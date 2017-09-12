package com.yiwugou.dbbus.core;

import java.util.Map;

public interface EventConsumer {
    boolean onDelete(DbbusEvent event);

    boolean onInsert(DbbusEvent event, Map<String, Object> data);

    boolean onUpdate(DbbusEvent event, Map<String, Object> data);
}
