package com.yiwugou.dbbus.core.cluster;

import com.yiwugou.dbbus.core.config.ClusterConfig;
import com.yiwugou.dbbus.core.enums.ClusterType;

public class ClusterLockCreater {
    public static ClusterLock create(ClusterConfig clusterConfig) {
        if (clusterConfig.getClusterType() == ClusterType.ZOOKEEPER) {
            return new ZookeeperClusterLock(clusterConfig);
        }
        if (clusterConfig.getClusterType() == ClusterType.REDIS) {
            return new RedisClusterLock(clusterConfig);
        }
        return new NoneClusterLock();
    }
}
