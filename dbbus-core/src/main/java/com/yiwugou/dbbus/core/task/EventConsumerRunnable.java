package com.yiwugou.dbbus.core.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yiwugou.dbbus.core.BeanCreater;
import com.yiwugou.dbbus.core.DataContainer;
import com.yiwugou.dbbus.core.DbbusEvent;
import com.yiwugou.dbbus.core.EventConsumer;
import com.yiwugou.dbbus.core.config.Config;
import com.yiwugou.dbbus.core.config.IdColumns;
import com.yiwugou.dbbus.core.enums.Action;
import com.yiwugou.dbbus.core.jdbc.JdbcTemplate;

public class EventConsumerRunnable implements Runnable, Executeable {
    private static final Logger logger = LoggerFactory.getLogger(EventConsumerRunnable.class);

    private static final Executor EXECUTOR = Executors.newSingleThreadExecutor();

    private JdbcTemplate jdbcTemplate;

    private EventConsumer eventConsumer;

    private Config config;

    public EventConsumerRunnable(JdbcTemplate jdbcTemplate, BeanCreater beanCreater, Config config) {
        this.jdbcTemplate = jdbcTemplate;
        this.eventConsumer = beanCreater.getEventConsumer();
        if (this.eventConsumer == null) {
            throw new RuntimeException("eventConsumer must not be null");
        }
        this.config = config;
    }

    @Override
    public void run() {
        List<DbbusEvent> events = new ArrayList<>();
        DataContainer.eventAfterMergeQueue().drainTo(events);
        for (DbbusEvent event : events) {
            if (Action.DELETE == event.getAction()) {
                this.eventConsumer.onDelete(event);
            } else {
                String tableName = event.getTableName();
                IdColumns idColumns = this.config.getTableConfig().getIdColumns(tableName);
                if (idColumns == null) {
                    idColumns = new IdColumns();
                }
                Map<String, Object> data = this.processDataMap(idColumns, event);
                if (Action.INSERT == event.getAction()) {
                    this.eventConsumer.onInsert(event, data);
                } else if (Action.UPDATE == event.getAction()) {
                    this.eventConsumer.onUpdate(event, data);
                } else {
                    logger.error("not support event action=" + event.getAction());
                }
            }
        }
    }

    private Map<String, Object> processDataMap(IdColumns idColumns, DbbusEvent event) {
        Map<String, Object> data = null;
        String tableName = event.getTableName();
        String id = event.getId();
        if (idColumns.getEnable() != null && idColumns.getEnable()) {
            String sql = "select " + idColumns.getColumns() + " from " + tableName + " where " + idColumns.getId()
                    + "=?";
            data = this.jdbcTemplate.queryForMap(sql, id);
        }
        return data;
    }

    @Override
    public void execute() {
        EXECUTOR.execute(this);
    }
}
