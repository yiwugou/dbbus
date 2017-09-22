package com.yiwugou.dbbus.core.task;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yiwugou.dbbus.core.enums.Status;
import com.yiwugou.dbbus.core.jdbc.JdbcTemplate;
import com.yiwugou.dbbus.core.start.Application;

/**
 *
 * EventClearTask
 *
 * @author zhanxiaoyong@yiwugou.com
 *
 * @since 2017年9月20日 上午8:57:40
 */
public class EventClearTask implements Runnable, Executeable {
    private static final Logger logger = LoggerFactory.getLogger(EventClearTask.class);
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);

    private JdbcTemplate jdbcTemplate;

    private Application application;

    public EventClearTask(JdbcTemplate jdbcTemplate, Application application) {
        this.jdbcTemplate = jdbcTemplate;
        this.application = application;
    }

    @Override
    public void run() {
        int result = this.jdbcTemplate.update("delete from dbbus_event where status=?", Status.READED.ordinal());
        logger.debug("event clear result=" + result);
    }

    @Override
    public void execute() {
        Long clearDelay = this.application.getConfig().getEventConfig().getClearDelay();
        if (clearDelay != null && clearDelay > 0) {
            this.executor.scheduleWithFixedDelay(this, 0, clearDelay, TimeUnit.MILLISECONDS);
        }
    }
}
