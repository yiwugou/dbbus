package com.yiwugou.dbbus.core.jdbc;

import javax.sql.DataSource;

import com.yiwugou.dbbus.core.config.JdbcConfig;

public interface DataSourceCreater {
    public DataSource create(JdbcConfig config);
}
