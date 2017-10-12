package com.yiwugou.dbbus.core.task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yiwugou.dbbus.core.DbbusEvent;
import com.yiwugou.dbbus.core.config.IdColumns;
import com.yiwugou.dbbus.core.consumer.EventConsumer;
import com.yiwugou.dbbus.core.enums.Action;
import com.yiwugou.dbbus.core.enums.Status;
import com.yiwugou.dbbus.core.jdbc.JdbcTemplate;
import com.yiwugou.dbbus.core.jdbc.RowMapper;
import com.yiwugou.dbbus.core.start.Application;

/**
 *
 * EventConsumerRunnable
 *
 * @author zhanxiaoyong@yiwugou.com
 *
 * @since 2017年9月20日 上午8:57:47
 */
public class EventConsumerRunnable implements Runnable, Executeable {
    private static final Logger logger = LoggerFactory.getLogger(EventConsumerRunnable.class);

    private static final Executor EXECUTOR = Executors.newSingleThreadExecutor();

    private JdbcTemplate jdbcTemplate;

    private Application application;

    public EventConsumerRunnable(JdbcTemplate jdbcTemplate, Application application) {
        this.jdbcTemplate = jdbcTemplate;
        this.application = application;
    }

    @Override
    public void run() {
        List<DbbusEvent> events = new ArrayList<>();
        this.application.getAfterMergeQueue().drainTo(events);
        for (DbbusEvent event : events) {
            boolean success = false;
            EventConsumer eventConsumer = this.application.getBeanCreater().getEventConsumerMap()
                    .get(event.getTableName());
            if (eventConsumer == null) {
                eventConsumer = this.application.getBeanCreater().getDefaultEventConsumer();
                if (eventConsumer == null) {
                    logger.error("the table {} eventConsumer and defaultEventConsumer is null", event.getTableName());
                    continue;
                }
            }
            try {
                if (Action.DELETE == event.getAction()) {
                    success = eventConsumer.onDelete(event);
                } else {
                    String tableName = event.getTableName();
                    IdColumns idColumns = this.application.getConfig().getTableConfig().getIdColumns(tableName);
                    if (idColumns == null) {
                        idColumns = new IdColumns();
                    }
                    Object data = this.processData(idColumns, event, eventConsumer.getRowMapper());
                    if (Action.INSERT == event.getAction()) {
                        success = eventConsumer.onInsert(event, data);
                    } else if (Action.UPDATE == event.getAction()) {
                        success = eventConsumer.onUpdate(event, data);
                    } else {
                        logger.error("not support event action={}", event.getAction());
                    }
                }
            } catch (Exception e) {
                logger.error("consumer error", e);
            }
            if (!success) {
                this.jdbcTemplate.update(this.application.getBeanCreater().getSqlCreater()
                        .getOneEventUpdateSql(Status.ERROR, event.getTxn()));
                logger.error("consumer failed! event=" + event);
            }
        }
    }

    private Object processData(IdColumns idColumns, DbbusEvent event, RowMapper rowMapper) {
        Object data = null;
        String tableName = event.getTableName();
        if (idColumns.getEnable() != null && idColumns.getEnable()) {
            String sql = this.application.getBeanCreater().getSqlCreater().getSelectSql(tableName,
                    idColumns.getColumns(), idColumns.getId(), event.getId());
            if (rowMapper == null) {
                data = this.jdbcTemplate.queryForMap(sql);
            } else {
                data = this.jdbcTemplate.queryForObject(sql, rowMapper);
            }
        }
        return data;
    }

    @Override
    public void execute() {
        EXECUTOR.execute(this);
    }
}
