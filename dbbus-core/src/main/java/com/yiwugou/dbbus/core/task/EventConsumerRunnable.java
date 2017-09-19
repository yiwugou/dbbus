package com.yiwugou.dbbus.core.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yiwugou.dbbus.core.DbbusEvent;
import com.yiwugou.dbbus.core.EventConsumer;
import com.yiwugou.dbbus.core.config.IdColumns;
import com.yiwugou.dbbus.core.enums.Action;
import com.yiwugou.dbbus.core.jdbc.JdbcTemplate;
import com.yiwugou.dbbus.core.start.Application;

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
            throw new RuntimeException("eventConsumer must not be null");
        }
    }

    @Override
    public void run() {
        List<DbbusEvent> events = new ArrayList<>();
        this.application.getAfterMergeQueue().drainTo(events);
        for (DbbusEvent event : events) {
            if (Action.DELETE == event.getAction()) {
                this.eventConsumer.onDelete(event);
            } else {
                String tableName = event.getTableName();
                IdColumns idColumns = this.application.getConfig().getTableConfig().getIdColumns(tableName);
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
