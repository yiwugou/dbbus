package com.yiwugou.dbbus.core.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yiwugou.dbbus.core.DbbusEvent;
import com.yiwugou.dbbus.core.DbbusException;
import com.yiwugou.dbbus.core.EventConsumer;
import com.yiwugou.dbbus.core.config.IdColumns;
import com.yiwugou.dbbus.core.enums.Action;
import com.yiwugou.dbbus.core.enums.Status;
import com.yiwugou.dbbus.core.jdbc.JdbcTemplate;
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

    private EventConsumer eventConsumer;

    private Application application;

    public EventConsumerRunnable(JdbcTemplate jdbcTemplate, Application application) {
        this.jdbcTemplate = jdbcTemplate;
        this.application = application;
        this.eventConsumer = application.getBeanCreater().getEventConsumer();
        if (this.eventConsumer == null) {
            throw new DbbusException("eventConsumer must not be null");
        }
    }

    @Override
    public void run() {
        List<DbbusEvent> events = new ArrayList<>();
        this.application.getAfterMergeQueue().drainTo(events);
        for (DbbusEvent event : events) {
            boolean success = false;
            try {
                if (Action.DELETE == event.getAction()) {
                    success = this.eventConsumer.onDelete(event);
                } else {
                    String tableName = event.getTableName();
                    IdColumns idColumns = this.application.getConfig().getTableConfig().getIdColumns(tableName);
                    if (idColumns == null) {
                        idColumns = new IdColumns();
                    }
                    Map<String, Object> data = this.processDataMap(idColumns, event);
                    if (Action.INSERT == event.getAction()) {
                        success = this.eventConsumer.onInsert(event, data);
                    } else if (Action.UPDATE == event.getAction()) {
                        success = this.eventConsumer.onUpdate(event, data);
                    } else {
                        logger.error("not support event action=" + event.getAction());
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

    private Map<String, Object> processDataMap(IdColumns idColumns, DbbusEvent event) {
        Map<String, Object> data = null;
        String tableName = event.getTableName();
        String id = event.getId();
        if (idColumns.getEnable() != null && idColumns.getEnable()) {
            String sql = this.application.getBeanCreater().getSqlCreater().getSelectSql(tableName,
                    idColumns.getColumns(), idColumns.getId(), id);
            data = this.jdbcTemplate.queryForMap(sql);
        }
        return data;
    }

    @Override
    public void execute() {
        EXECUTOR.execute(this);
    }
}
