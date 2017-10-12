package com.yiwugou.dbbus.example;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yiwugou.dbbus.core.DbbusEvent;
import com.yiwugou.dbbus.core.consumer.EventConsumer;
import com.yiwugou.dbbus.core.jdbc.RowMapper;

/**
 *
 * ExampleEventConsumer
 *
 * @author zhanxiaoyong@yiwugou.com
 *
 * @since 2017年9月20日 上午9:02:42
 */
public class ExampleEventConsumer implements EventConsumer<Map<String, Object>> {

    private static final Logger logger = LoggerFactory.getLogger(ExampleEventConsumer.class);

    @Override
    public boolean onDelete(DbbusEvent event) {
        logger.info("onDelete " + event);
        return true;
    }

    @Override
    public boolean onInsert(DbbusEvent event, Map<String, Object> data) {
        logger.info("onInsert " + event + ", data=" + data);
        return true;
    }

    @Override
    public boolean onUpdate(DbbusEvent event, Map<String, Object> data) {
        logger.info("onUpdate " + event + ", data=" + data);
        return true;
    }

    @Override
    public RowMapper<Map<String, Object>> getRowMapper() {
        // TODO Auto-generated method stub
        return null;
    }
}
