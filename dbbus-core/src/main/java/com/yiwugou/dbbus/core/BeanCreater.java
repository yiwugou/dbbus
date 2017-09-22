package com.yiwugou.dbbus.core;

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
    @Getter
    private EventConsumer eventConsumer;
    @Getter
    private SqlCreater sqlCreater;

    public static BeanCreaterBuilder builder() {
        return new BeanCreaterBuilder();
    }

    public static class BeanCreaterBuilder {
        private DataSourceCreater dataSourceCreater = new DruidDataSourceCreater();
        private EventConsumer eventConsumer;
        private SqlCreater sqlCreater = new OracleSqlCreater();

        public BeanCreaterBuilder dataSourceCreater(DataSourceCreater dataSourceCreater) {
            this.dataSourceCreater = dataSourceCreater;
            return this;
        }

        public BeanCreaterBuilder eventConsumer(EventConsumer eventConsumer) {
            this.eventConsumer = eventConsumer;
            return this;
        }

        public BeanCreaterBuilder sqlCreater(SqlCreater sqlCreater) {
            this.sqlCreater = sqlCreater;
            return this;
        }

        public BeanCreater build() {
            return new BeanCreater(this.dataSourceCreater, this.eventConsumer, this.sqlCreater);
        }
    }
}
