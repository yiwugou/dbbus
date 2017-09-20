package com.yiwugou.dbbus.core.config;

/**
 *
 * Constants
 *
 * @author zhanxiaoyong@yiwugou.com
 *
 * @since 2017年9月20日 上午8:56:19
 */
public class Constants {
    public static final String CONFIG_FILE = "dbbus.properties";

    public static final String JDBC_DRIVER = "jdbc.driver";
    public static final String JDBC_URL = "jdbc.url";
    public static final String JDBC_USERNAME = "jdbc.username";
    public static final String JDBC_PASSWORD = "jdbc.password";
    public static final String JDBC_MAX_ACTIVE = "jdbc.maxActive";
    public static final String JDBC_MIN_IDLE = "jdbc.minIdle";

    public static final String EVENT_MERGE_UPDATE = "event.mergeUpdate";
    public static final String EVENT_MAX_ROW_NUM = "event.maxRowNum";
    public static final String EVENT_QUEUE_CAPACITY = "event.queueCapacity";
    public static final String EVENT_PULLER_POOL_SIZE = "event.pullerPoolSize";
    public static final String EVENT_PULLER_DELAY = "event.pullerDelay";
    public static final String EVENT_CLEAR_DELAY = "event.clearDelay";

    public static final String CLUSTER_TYPE = "cluster.type";
    public static final String CLUSTER_HOST_PORT = "cluster.hostPort";

    public static final String TABLE_ID = "^table\\.(.+)\\.id$";
    public static final String TABLE_COLUMNS = "^table\\.(.+)\\.columns$";
    public static final String TABLE_ENABLE = "^table\\.(.+)\\.enable$";

}
