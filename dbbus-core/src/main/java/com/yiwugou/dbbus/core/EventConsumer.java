package com.yiwugou.dbbus.core;

import java.util.Map;

/**
 *
 * EventConsumer
 *
 * @author zhanxiaoyong@yiwugou.com
 *
 * @since 2017年9月20日 上午8:55:20
 */
public interface EventConsumer {
    boolean onDelete(DbbusEvent event);

    boolean onInsert(DbbusEvent event, Map<String, Object> data);

    boolean onUpdate(DbbusEvent event, Map<String, Object> data);
}
