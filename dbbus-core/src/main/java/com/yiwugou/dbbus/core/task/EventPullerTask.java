package com.yiwugou.dbbus.core.task;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yiwugou.dbbus.core.DbbusEvent;
import com.yiwugou.dbbus.core.cluster.ClusterLock;
import com.yiwugou.dbbus.core.cluster.ClusterLockCreater;
import com.yiwugou.dbbus.core.enums.Action;
import com.yiwugou.dbbus.core.enums.Status;
import com.yiwugou.dbbus.core.jdbc.JdbcTemplate;
import com.yiwugou.dbbus.core.jdbc.RowMapper;
import com.yiwugou.dbbus.core.start.Application;

import lombok.Getter;

/**
 *
 * EventPullerTask
 *
 * @author zhanxiaoyong@yiwugou.com
 *
 * @since 2017年9月20日 上午8:57:57
 */
public class EventPullerTask implements Runnable, Executeable {
    private static final Logger logger = LoggerFactory.getLogger(EventPullerTask.class);

    private ClusterLock clusterLock;

    private Application application;

    public EventPullerTask(Application application) {
        this.application = application;
        this.initData();
    }

    private void initData() {
        this.jdbcTemplate = new JdbcTemplate(this.application.getBeanCreater().getDataSourceCreater()
                .create(this.application.getConfig().getJdbcConfig()));
        this.executor = Executors
                .newScheduledThreadPool(this.application.getConfig().getEventConfig().getPullerPoolSize());
        this.clusterLock = ClusterLockCreater.create(this.application.getConfig().getClusterConfig());
    }

    @Getter
    private JdbcTemplate jdbcTemplate;
    private ScheduledExecutorService executor;

    @Override
    public void run() {
        if (this.clusterLock.tryLock()) {
            try {
                this.accessDb();
            } finally {
                this.clusterLock.unLock();
            }
        }
    }

    private void accessDb() {
        List<DbbusEvent> events = this.jdbcTemplate.queryForList(
                this.application.getBeanCreater().getSqlCreater().getEventLimitSql(Status.UNREAD,
                        this.application.getConfig().getEventConfig().getMaxRowNum()),
                (RowMapper<DbbusEvent>) (rs, rowNum) -> {
                    DbbusEvent de = new DbbusEvent();
                    de.setTxn(rs.getLong("txn"));
                    de.setTableName(rs.getString("tableName"));
                    de.setId(rs.getString("id"));
                    de.setAction(Action.parse(rs.getInt("action")));
                    de.setStatus(Status.parse(rs.getInt("status")));
                    de.setTs(rs.getLong("ts"));
                    return de;
                });
        if (events != null && !events.isEmpty()) {
            logger.info("dbbus event size=" + events.size() + ", events=" + events);
            Long minTxn = events.get(0).getTxn();
            Long maxTxn = events.get(events.size() - 1).getTxn();
            int result = this.jdbcTemplate.update(
                    this.application.getBeanCreater().getSqlCreater().getEventUpdateSql(Status.READED, minTxn, maxTxn));
            logger.info("minTxn=" + minTxn + ", maxTxn=" + maxTxn + ", update result=" + result);
            this.application.getBeforeMergeQueue().addAll(events);
            new EventMergeRunnable(this.jdbcTemplate, this.application).execute();
        } else {
            logger.debug("event is empty");
        }
    }

    @Override
    public void execute() {
        this.executor.scheduleWithFixedDelay(this, 0, this.application.getConfig().getEventConfig().getPullerDelay(),
                TimeUnit.MILLISECONDS);
        new EventClearTask(this.jdbcTemplate, this.application).execute();
    }

}
