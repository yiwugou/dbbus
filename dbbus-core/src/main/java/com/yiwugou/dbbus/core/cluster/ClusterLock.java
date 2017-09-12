package com.yiwugou.dbbus.core.cluster;

public interface ClusterLock {
    void lock();

    boolean tryLock();

    void unLock();
}
