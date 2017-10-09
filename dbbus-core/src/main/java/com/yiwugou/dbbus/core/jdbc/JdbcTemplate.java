package com.yiwugou.dbbus.core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.yiwugou.dbbus.core.DbbusException;
import com.yiwugou.dbbus.core.util.CommonUtils;

/**
 *
 * JdbcTemplate
 *
 * @author zhanxiaoyong@yiwugou.com
 *
 * @since 2017年9月20日 上午8:57:24
 */
public class JdbcTemplate {
    private DataSource dataSource;

    public JdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public int update(String sql, Object... param) {
        Connection con = null;
        PreparedStatement ps = null;
        int result = 0;
        try {
            con = this.dataSource.getConnection();
            ps = this.createStatement(con, sql, param);
            result = ps.executeUpdate();
        } catch (SQLException e) {
            throw new DbbusException(e);
        } finally {
            this.closeAll(con, ps, null);
        }
        return result;
    }

    public Map<String, Object> queryForMap(String sql, Object... param) {
        List<Map<String, Object>> list = this.queryForList(sql, param);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public <T> List<T> queryForList(String sql, RowMapper<T> rowMapper, Object... param) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<T> results = new ArrayList<>();
        try {
            con = this.dataSource.getConnection();
            ps = this.createStatement(con, sql, param);
            rs = ps.executeQuery();
            int rowNum = 0;
            while (rs.next()) {
                results.add(rowMapper.mapRow(rs, rowNum++));
            }
        } catch (SQLException e) {
            throw new DbbusException(e);
        } finally {
            this.closeAll(con, ps, rs);
        }
        return results;
    }

    public <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... param) {
        List<T> list = this.queryForList(sql, rowMapper, param);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public List<Map<String, Object>> queryForList(String sql, Object... param) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            con = this.dataSource.getConnection();
            ps = this.createStatement(con, sql, param);
            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int len = rsmd.getColumnCount();
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                for (int i = 1; i <= len; i++) {
                    map.put(rsmd.getColumnName(i), rs.getObject(i));
                }
                list.add(map);
            }
        } catch (SQLException e) {
            throw new DbbusException(e);
        } finally {
            this.closeAll(con, ps, rs);
        }
        return list;
    }

    private PreparedStatement createStatement(Connection con, String sql, Object... param) throws SQLException {
        PreparedStatement ps = con.prepareStatement(sql);
        int i = 1;
        for (Object obj : param) {
            ps.setObject(i++, obj);
        }
        return ps;
    }

    private void closeAll(Connection con, Statement stm, ResultSet rs) {
        CommonUtils.close(rs);
        CommonUtils.close(stm);
        CommonUtils.close(con);
    }

}
