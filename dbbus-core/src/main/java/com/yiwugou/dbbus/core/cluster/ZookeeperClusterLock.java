package com.yiwugou.dbbus.core.cluster;

import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreMutex;
import org.apache.curator.retry.RetryNTimes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yiwugou.dbbus.core.config.ClusterConfig;

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
public class ZookeeperClusterLock implements ClusterLock {
    private static final Logger logger = LoggerFactory.getLogger(ZookeeperClusterLock.class);
    private static final String PATH = "/dbbus/zk/";

    private CuratorFramework client;

    private InterProcessLock lock;

    public ZookeeperClusterLock(ClusterConfig clusterConfig) {
        this.client = CuratorFrameworkFactory.newClient(clusterConfig.getHostPort(), new RetryNTimes(2, 500));
        client.start();
        String path = PATH + Integer.toHexString(hashCode());
        lock = new InterProcessSemaphoreMutex(client, path);
        this.unLock();
    }

    @Override
    public void lock() {
        try {
            lock.acquire();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean tryLock() {
        try {
            return lock.acquire(1, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            logger.error("tryLock error", e);
        }
        return false;
    }

    @Override
    public void unLock() {
        try {
            lock.release();
        } catch (Exception e) {
            logger.error("unLock error", e);
        }
    }
}
