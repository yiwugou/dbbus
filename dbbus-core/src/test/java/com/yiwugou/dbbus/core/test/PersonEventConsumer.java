package com.yiwugou.dbbus.core.test;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.yiwugou.dbbus.core.DbbusEvent;
import com.yiwugou.dbbus.core.consumer.EventConsumer;
import com.yiwugou.dbbus.core.jdbc.RowMapper;

/**
 *
 * PersonEventConsumer
 *
 * @author zhanxiaoyong@yiwugou.com
 *
 * @since 2017年10月12日 上午8:36:16
 */
public class PersonEventConsumer implements EventConsumer<Person> {

    @Override
    public boolean onDelete(DbbusEvent event) {
        System.err.println("onDelete " + event);
        return true;
    }

    @Override
    public boolean onInsert(DbbusEvent event, Person data) {
        System.err.println("onInsert " + event + ", data=" + data);
        return true;
    }

    @Override
    public boolean onUpdate(DbbusEvent event, Person data) {
        System.err.println("onUpdate " + event + ", data=" + data);
        return true;
    }

    @Override
    public RowMapper<Person> getRowMapper() {
        RowMapper<Person> rowMapper = new RowMapper<Person>() {
            @Override
            public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
                Person person = new Person();
                person.setId(PersonEventConsumer.this.resultSetToLong(rs, "ID"));
                person.setPersonAge(PersonEventConsumer.this.resultSetToInteger(rs, "PERSON_AGE"));
                person.setPersonName(PersonEventConsumer.this.resultSetToString(rs, "PERSON_NAME"));
                return person;
            }
        };
        return rowMapper;
    }

}
