package com.yiwugou.dbbus.core.test;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.alibaba.druid.pool.DruidDataSource;
import com.yiwugou.dbbus.core.jdbc.JdbcTemplate;

public class JdbcTemplateTest {
    @Test
    public void test1() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        dataSource.setUrl("jdbc:oracle:thin:@10.6.2.72:1521:orcl");
        dataSource.setUsername("dbbus");
        dataSource.setPassword("dbbus");
        dataSource.setMaxActive(100);
        dataSource.setMinIdle(5);

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        jdbcTemplate.update("insert into t_person(id,person_name) values(?,?)", 1, "hello1");
        jdbcTemplate.update("insert into t_person(id,person_name) values(?,?)", 2, "hello2");

        int updateResult = jdbcTemplate.update("update t_person set person_name=? where id=?", "world1", 1);
        System.err.println(updateResult);

        List<Map<String, Object>> queryForList = jdbcTemplate.queryForList("select * from t_person where id>?", 0);
        System.err.println(queryForList);

        Map<String, Object> queryForMap = jdbcTemplate.queryForMap("select * from t_person where id=?", 1);
        System.err.println(queryForMap);

        int deleteResult = jdbcTemplate.update("delete from t_person where id>?", 0);
        System.err.println(deleteResult);
    }
}
