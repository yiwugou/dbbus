package com.yiwugou.dbbus.core;

import com.yiwugou.dbbus.core.jdbc.DataSourceCreater;
import com.yiwugou.dbbus.core.jdbc.DruidDataSourceCreater;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BeanCreater {
    @Getter
    private DataSourceCreater dataSourceCreater;
    @Getter
    private EventConsumer eventConsumer;

    public static BeanCreaterBuilder builder() {
        return new BeanCreaterBuilder();
    }

    public static class BeanCreaterBuilder {
        private DataSourceCreater dataSourceCreater = new DruidDataSourceCreater();
        private EventConsumer eventConsumer;

        public BeanCreaterBuilder dataSourceCreater(DataSourceCreater dataSourceCreater) {
            this.dataSourceCreater = dataSourceCreater;
            return this;
        }

        public BeanCreaterBuilder eventConsumer(EventConsumer eventConsumer) {
            this.eventConsumer = eventConsumer;
            return this;
        }

        public BeanCreater build() {
            return new BeanCreater(this.dataSourceCreater, this.eventConsumer);
        }
    }
}
