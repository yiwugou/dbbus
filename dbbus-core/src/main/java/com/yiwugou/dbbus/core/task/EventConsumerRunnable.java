package com.yiwugou.dbbus.core.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yiwugou.dbbus.core.DataContainer;
import com.yiwugou.dbbus.core.DbbusEvent;
import com.yiwugou.dbbus.core.EventConsumer;
import com.yiwugou.dbbus.core.enums.Action;
import com.yiwugou.dbbus.core.jdbc.JdbcTemplate;

public class EventConsumerRunnable implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(EventConsumerRunnable.class);

    private static final Executor EXECUTOR = Executors.newSingleThreadExecutor();

    private JdbcTemplate jdbcTemplate;

    private EventConsumer eventConsumer;

    public EventConsumerRunnable(JdbcTemplate jdbcTemplate, EventConsumer eventConsumer) {
        this.jdbcTemplate = jdbcTemplate;
        this.eventConsumer = eventConsumer;
    }

    @Override
    public void run() {
        List<DbbusEvent> events = new ArrayList<>();
        DataContainer.eventAfterMergeQueue().drainTo(events);
        for (DbbusEvent event : events) {
            if (Action.DELETE == event.getAction()) {
                eventConsumer.onDelete(event);
            } else {
                String table = event.getTableName();
                String id = event.getId();
                String sql = "select * from " + table + " where id=?";
                Map<String, Object> data = this.jdbcTemplate.queryForMap(sql, id);
                if (Action.INSERT == event.getAction()) {
                    eventConsumer.onInsert(event, data);
                } else if (Action.UPDATE == event.getAction()) {
                    eventConsumer.onUpdate(event, data);
                } else {
                    logger.error("not support event action=" + event.getAction());
                }
            }
        }
    }

    public void execute() {
        EXECUTOR.execute(this);
    }
}
