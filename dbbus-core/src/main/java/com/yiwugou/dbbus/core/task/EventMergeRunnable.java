package com.yiwugou.dbbus.core.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yiwugou.dbbus.core.DbbusEvent;
import com.yiwugou.dbbus.core.TableId;
import com.yiwugou.dbbus.core.enums.Action;
import com.yiwugou.dbbus.core.jdbc.JdbcTemplate;
import com.yiwugou.dbbus.core.start.Application;

/**
 *
 * EventMergeRunnable
 *
 * @author zhanxiaoyong@yiwugou.com
 *
 * @since 2017年9月20日 上午8:57:52
 */
public class EventMergeRunnable implements Runnable, Executeable {
    private static final Logger logger = LoggerFactory.getLogger(EventMergeRunnable.class);

    private static final Executor EXECUTOR = Executors.newSingleThreadExecutor();
    private JdbcTemplate jdbcTemplate;

    private Application application;

    public EventMergeRunnable(JdbcTemplate jdbcTemplate, Application application) {
        this.jdbcTemplate = jdbcTemplate;
        this.application = application;
    }

    @Override
    public void run() {
        List<DbbusEvent> events = new ArrayList<>();
        this.application.getBeforeMergeQueue().drainTo(events);
        if (events != null && !events.isEmpty()) {
            List<DbbusEvent> afterMerge = this.mergeEvent(events);
            this.application.getAfterMergeQueue().addAll(afterMerge);
            new EventConsumerRunnable(this.jdbcTemplate, this.application).execute();
        }
    }

    private List<DbbusEvent> mergeEvent(List<DbbusEvent> events) {
        List<DbbusEvent> afterMerge = new LinkedList<>();
        Map<TableId, DbbusEvent> updateMap = new HashMap<>();
        for (DbbusEvent event : events) {
            if (Action.UPDATE == event.getAction()) {
                TableId tableId = new TableId(event.getTableName(), event.getId());
                DbbusEvent exist = updateMap.get(tableId);
                if (exist != null) {
                    Iterator<DbbusEvent> iterator = afterMerge.iterator();
                    while (iterator.hasNext()) {
                        DbbusEvent i = iterator.next();
                        if (this.isEquals(exist, i)) {
                            iterator.remove();
                        }
                    }
                }
                updateMap.put(tableId, event);
            }
            afterMerge.add(event);
        }
        logger.info("afterMerge size=" + afterMerge.size() + ", events=" + afterMerge);
        return afterMerge;
    }

    private boolean isEquals(DbbusEvent src, DbbusEvent desc) {
        return src.getTableName().equals(desc.getTableName()) && src.getId().equals(desc.getId())
                && src.getAction() == desc.getAction();
    }

    @Override
    public void execute() {
        EXECUTOR.execute(this);
    }

}
