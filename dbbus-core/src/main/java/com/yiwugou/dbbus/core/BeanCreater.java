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

    // @Getter
    // private ClusterLock distributedLock;

    public static BeanCreaterBuilder builder() {
        return new BeanCreaterBuilder();
    }

    public static class BeanCreaterBuilder {
        private DataSourceCreater dataSourceCreater = new DruidDataSourceCreater();
        // private ClusterLock distributedLock = new NoneClusterLock();

        public BeanCreaterBuilder dataSourceCreater(DataSourceCreater dataSourceCreater) {
            this.dataSourceCreater = dataSourceCreater;
            return this;
        }

        // public BeanCreaterBuilder distributedLock(ClusterLock
        // distributedLock) {
        // this.distributedLock = distributedLock;
        // return this;
        // }

        public BeanCreater build() {
            return new BeanCreater(dataSourceCreater);
        }
    }
}
