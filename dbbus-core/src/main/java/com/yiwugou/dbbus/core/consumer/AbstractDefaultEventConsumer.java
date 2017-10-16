package com.yiwugou.dbbus.core.consumer;

import java.util.Map;

import com.yiwugou.dbbus.core.jdbc.RowMapper;

/**
 *
 * AbstractDefaultEventConsumer
 *
 * @author zhanxiaoyong@yiwugou.com
 *
 * @since 2017年10月16日 下午4:25:46
 */
public abstract class AbstractDefaultEventConsumer implements EventConsumer<Map<String, Object>> {

    @Override
    public RowMapper<Map<String, Object>> getRowMapper() {
        return null;
    }

}
