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
                String id = event.getId();
                IdColumns idColumns = this.config.getTableConfig().getIdColumns(tableName);
                if (idColumns == null) {
                    idColumns = new IdColumns();
                }
                String sql = "select " + idColumns.getColumns() + " from " + tableName + " where " + idColumns.getId()
                        + "=?";
                Map<String, Object> data = this.jdbcTemplate.queryForMap(sql, id);
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

    @Override
    public void execute() {
        EXECUTOR.execute(this);
    }
}
