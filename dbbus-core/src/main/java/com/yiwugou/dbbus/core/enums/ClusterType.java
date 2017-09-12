package com.yiwugou.dbbus.core.enums;

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
