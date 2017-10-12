package com.yiwugou.dbbus.core;

import java.util.HashMap;
import java.util.Map;

import com.yiwugou.dbbus.core.consumer.AbstractDefaultEventConsumer;
import com.yiwugou.dbbus.core.consumer.EventConsumer;
import com.yiwugou.dbbus.core.jdbc.DataSourceCreater;
import com.yiwugou.dbbus.core.jdbc.DruidDataSourceCreater;
import com.yiwugou.dbbus.core.sql.OracleSqlCreater;
import com.yiwugou.dbbus.core.sql.SqlCreater;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 *
 * BeanCreater
 *
 * @author zhanxiaoyong@yiwugou.com
 *
 * @since 2017年9月20日 上午8:55:10
 */
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BeanCreater {
    @Getter
    private DataSourceCreater dataSourceCreater;
    /**
     * table --> eventConsumer
     */
    @Getter
    private Map<String, EventConsumer> eventConsumerMap;
    @Getter
    private AbstractDefaultEventConsumer defaultEventConsumer;
    @Getter
    private SqlCreater sqlCreater;

    public static BeanCreaterBuilder builder() {
        return new BeanCreaterBuilder();
    }

    public static class BeanCreaterBuilder {
        private DataSourceCreater dataSourceCreater = new DruidDataSourceCreater();
        private Map<String, EventConsumer> eventConsumerMap = new HashMap<>();
        private AbstractDefaultEventConsumer defaultEventConsumer;
        private SqlCreater sqlCreater = new OracleSqlCreater();

        public BeanCreaterBuilder dataSourceCreater(DataSourceCreater dataSourceCreater) {
            this.dataSourceCreater = dataSourceCreater;
            return this;
        }

        public BeanCreaterBuilder defaultEventConsumer(AbstractDefaultEventConsumer defaultEventConsumer) {
            this.defaultEventConsumer = defaultEventConsumer;
            return this;
        }

        public BeanCreaterBuilder eventConsumerMap(Map<String, EventConsumer> eventConsumerMap) {
            this.eventConsumerMap = eventConsumerMap;
            return this;
        }

        public BeanCreaterBuilder sqlCreater(SqlCreater sqlCreater) {
            this.sqlCreater = sqlCreater;
            return this;
        }

        public BeanCreater build() {
            return new BeanCreater(this.dataSourceCreater, this.eventConsumerMap, this.defaultEventConsumer,
                    this.sqlCreater);
        }
    }
}
