package com.yiwugou.dbbus.core.jdbc;

import javax.sql.DataSource;

import com.yiwugou.dbbus.core.config.JdbcConfig;

/**
 *
 * DataSourceCreater
 *
 * @author zhanxiaoyong@yiwugou.com
 *
 * @since 2017年9月20日 上午8:57:12
 */
public interface DataSourceCreater {
    public DataSource create(JdbcConfig config);
}
