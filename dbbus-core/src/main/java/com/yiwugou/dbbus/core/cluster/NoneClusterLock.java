package com.yiwugou.dbbus.core.cluster;

/**
 *
 * NoneClusterLock
 *
 * @author zhanxiaoyong@yiwugou.com
 *
 * @since 2017年9月20日 上午8:55:49
 */
public class NoneClusterLock implements ClusterLock {

    @Override
    public void lock() {

    }

    @Override
    public boolean tryLock() {
        return true;
    }

    @Override
    public void unLock() {

    }

}
