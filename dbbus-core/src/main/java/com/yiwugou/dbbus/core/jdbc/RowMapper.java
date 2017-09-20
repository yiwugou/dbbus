package com.yiwugou.dbbus.core.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * RowMapper
 *
 * @author zhanxiaoyong@yiwugou.com
 *
 * @since 2017年9月20日 上午8:57:30
 */
public interface RowMapper<T> {
    T mapRow(ResultSet rs, int rowNum) throws SQLException;
}
