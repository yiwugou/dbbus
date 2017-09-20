package com.yiwugou.dbbus.core.enums;

/**
 *
 * ClusterType
 *
 * @author zhanxiaoyong@yiwugou.com
 *
 * @since 2017年9月20日 上午8:57:03
 */
public enum ClusterType {
    NONE, ZOOKEEPER, REDIS;

    public static ClusterType parse(String type) {
        for (ClusterType a : ClusterType.values()) {
            if (a.toString().equalsIgnoreCase(type)) {
                return a;
            }
        }
        throw new IllegalArgumentException("no ClusterType suppored, ClusterType=" + type);
    }
}
