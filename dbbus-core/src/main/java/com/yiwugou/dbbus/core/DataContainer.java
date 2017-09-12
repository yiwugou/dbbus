package com.yiwugou.dbbus.core;

import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class DataContainer {

    private static BlockingQueue<DbbusEvent> EVENT_BEFORE_MERGE_QUEUE = null;
    private static BlockingQueue<DbbusEvent> EVENT_AFTER_MERGE_QUEUE = null;

    private static Properties PROPERTIES = null;

    public static void initProperties(Properties properties) {
        if (PROPERTIES == null) {
            PROPERTIES = properties;
        }
    }

    public static Properties properties() {
        return PROPERTIES;
    }

    public static void initEventQueue(Integer capacity) {
        if (EVENT_BEFORE_MERGE_QUEUE == null) {
            EVENT_BEFORE_MERGE_QUEUE = new LinkedBlockingQueue<>(capacity);
        }
        if (EVENT_AFTER_MERGE_QUEUE == null) {
            EVENT_AFTER_MERGE_QUEUE = new LinkedBlockingQueue<>(capacity);
        }
    }

    public static BlockingQueue<DbbusEvent> eventAfterMergeQueue() {
        return EVENT_AFTER_MERGE_QUEUE;
    }

    public static BlockingQueue<DbbusEvent> eventBeforeMergeQueue() {
        return EVENT_BEFORE_MERGE_QUEUE;
    }
}
