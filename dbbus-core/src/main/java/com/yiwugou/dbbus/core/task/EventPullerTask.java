package com.yiwugou.dbbus.core.task;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yiwugou.dbbus.core.BeanCreater;
import com.yiwugou.dbbus.core.DataContainer;
import com.yiwugou.dbbus.core.DbbusEvent;
import com.yiwugou.dbbus.core.EventConsumer;
import com.yiwugou.dbbus.core.cluster.ClusterLock;
import com.yiwugou.dbbus.core.cluster.ClusterLockCreater;
import com.yiwugou.dbbus.core.config.Config;
import com.yiwugou.dbbus.core.enums.Action;
import com.yiwugou.dbbus.core.enums.Status;
import com.yiwugou.dbbus.core.jdbc.JdbcTemplate;
import com.yiwugou.dbbus.core.jdbc.RowMapper;

public class EventPullerTask implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(EventPullerTask.class);

    private EventConsumer eventConsumer;

    private BeanCreater beanCreater;

    private Config config;

    private ClusterLock clusterLock;

    public EventPullerTask(Config config, BeanCreater beanCreater) {
        this.config = config;
        this.beanCreater = beanCreater;
        this.initData();
    }

    private void initData() {
        this.jdbcTemplate = new JdbcTemplate(beanCreater.getDataSourceCreater().create(config.getJdbcConfig()));
        this.executor = Executors.newScheduledThreadPool(config.getEventConfig().getPullerPoolSize());
        try {
            this.eventConsumer = (EventConsumer) Class.forName(config.getEventConfig().getConsumerClass())
                    .newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.clusterLock = ClusterLockCreater.create(this.config.getClusterConfig());
    }

    private JdbcTemplate jdbcTemplate;
    private ScheduledExecutorService executor;

    @Override
    public void run() {
        if (clusterLock.tryLock()) {
            try {
                accessDb();
            } finally {
                clusterLock.unLock();
            }
        }
    }

    private void accessDb() {
        List<DbbusEvent> events = this.jdbcTemplate.queryForList(
                "select * from (select txn,table_name as tableName,id,action,status,ts from dbbus_event where status=? order by txn asc) where rownum<=?",
                (RowMapper<DbbusEvent>) (rs, rowNum) -> {
                    DbbusEvent de = new DbbusEvent();
                    de.setTxn(rs.getLong("txn"));
                    de.setTableName(rs.getString("tableName"));
                    de.setId(rs.getString("id"));
                    de.setAction(Action.parse(rs.getInt("action")));
                    de.setStatus(Status.parse(rs.getInt("status")));
                    de.setTs(rs.getLong("ts"));
                    return de;
                }, Status.UNREAD.ordinal(), config.getEventConfig().getMaxRowNum());
        if (events != null && !events.isEmpty()) {
            logger.info("dbbus event size=" + events.size() + ", events=" + events);
            Long minTxn = events.get(0).getTxn();
            Long maxTxn = events.get(events.size() - 1).getTxn();
            logger.info("minTxn=" + minTxn + ",maxTxn=" + maxTxn);
            int result = this.jdbcTemplate.update("update dbbus_event set status=? where txn>=? and txn<=?",
                    Status.READED.ordinal(), minTxn, maxTxn);
            logger.info("update result=" + result);
            DataContainer.eventBeforeMergeQueue().addAll(events);
            new EventMergeRunnable(jdbcTemplate, eventConsumer).execute();
        } else {
            logger.debug("event is empty");
        }
    }

    public void execute() {
        executor.scheduleWithFixedDelay(this, 0, config.getEventConfig().getPullerDelay(), TimeUnit.MILLISECONDS);
    }

}
