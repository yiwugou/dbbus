package com.yiwugou.dbbus.core.cluster;

/**
 *
 * ClusterLock
 *
 * @author zhanxiaoyong@yiwugou.com
 *
 * @since 2017年9月20日 上午8:55:36
 */
public interface ClusterLock {
    void lock();

    boolean tryLock();

    void unLock();
}
