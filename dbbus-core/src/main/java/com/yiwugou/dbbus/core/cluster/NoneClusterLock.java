package com.yiwugou.dbbus.core.cluster;

/**
 *
 * <pre>
 * ZookeeperDistributedLock
 * use Apache Curator
 * </pre>
 *
 * @author zhanxiaoyong@yiwugou.com
 *
 * @since 2017年9月8日 下午2:05:51
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
