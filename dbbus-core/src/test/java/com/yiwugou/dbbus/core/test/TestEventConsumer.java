package com.yiwugou.dbbus.core.test;

import java.util.Map;

import org.apache.log4j.Logger;

import com.yiwugou.dbbus.core.DbbusEvent;
import com.yiwugou.dbbus.core.EventConsumer;

public class TestEventConsumer implements EventConsumer {

    private static final Logger logger = Logger.getLogger(TestEventConsumer.class);

    @Override
    public boolean onDelete(DbbusEvent event) {
        System.err.println("onDelete " + event);
        return true;
    }

    @Override
    public boolean onInsert(DbbusEvent event, Map<String, Object> data) {
        System.err.println("onInsert " + event + ", data=" + data);
        return true;
    }

    @Override
    public boolean onUpdate(DbbusEvent event, Map<String, Object> data) {
        System.err.println("onUpdate " + event + ", data=" + data);
        return true;
    }

}