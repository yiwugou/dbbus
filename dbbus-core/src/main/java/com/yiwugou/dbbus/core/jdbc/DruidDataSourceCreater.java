package com.yiwugou.dbbus.core.jdbc;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSource;
import com.yiwugou.dbbus.core.config.JdbcConfig;

/**
 *
 * DruidDataSourceCreater
 *
 * @author zhanxiaoyong@yiwugou.com
 *
 * @since 2017年9月20日 上午8:57:20
 */
public class DruidDataSourceCreater implements DataSourceCreater {
    @Override
    public DataSource create(JdbcConfig config) {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(config.getDriver());
        dataSource.setUrl(config.getUrl());
        dataSource.setUsername(config.getUsername());
        dataSource.setPassword(config.getPassword());
        dataSource.setMaxActive(config.getMaxActive());
        dataSource.setMinIdle(config.getMinIdle());
        return dataSource;
    }
}
