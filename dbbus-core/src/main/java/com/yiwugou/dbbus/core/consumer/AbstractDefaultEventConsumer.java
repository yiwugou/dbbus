package com.yiwugou.dbbus.core.consumer;

import java.util.Map;

import com.yiwugou.dbbus.core.jdbc.RowMapper;

public abstract class AbstractDefaultEventConsumer implements EventConsumer<Map<String, Object>> {

    @Override
    public RowMapper<Map<String, Object>> getRowMapper() {
        return null;
    }

}
