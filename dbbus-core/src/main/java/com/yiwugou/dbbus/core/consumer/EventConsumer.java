package com.yiwugou.dbbus.core.consumer;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.yiwugou.dbbus.core.DbbusEvent;
import com.yiwugou.dbbus.core.jdbc.RowMapper;

/**
 *
 * EventConsumer
 *
 * @author zhanxiaoyong@yiwugou.com
 *
 * @since 2017年9月20日 上午8:55:20
 */
public interface EventConsumer<T> {
    RowMapper<T> getRowMapper();

    boolean onDelete(DbbusEvent event);

    boolean onInsert(DbbusEvent event, T data);

    boolean onUpdate(DbbusEvent event, T data);

    default Long resultSetToLong(ResultSet rs, String columnName) throws SQLException {
        Object obj = rs.getObject(columnName);
        if (obj != null) {
            return Long.valueOf(obj.toString());
        }
        return null;
    }

    default String resultSetToString(ResultSet rs, String columnName) throws SQLException {
        Object obj = rs.getObject(columnName);
        if (obj != null) {
            return obj.toString();
        }
        return null;
    }

    default Integer resultSetToInteger(ResultSet rs, String columnName) throws SQLException {
        Object obj = rs.getObject(columnName);
        if (obj != null) {
            return Integer.valueOf(obj.toString());
        }
        return null;
    }

}
