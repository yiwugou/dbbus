package com.yiwugou.dbbus.core.cluster;

import com.yiwugou.dbbus.core.config.ClusterConfig;

import redis.clients.jedis.Jedis;

/**
 *
 * RedisClusterLock
 *
 * @author zhanxiaoyong@yiwugou.com
 *
 * @since 2017年9月20日 上午8:49:23
 */
public class RedisClusterLock implements ClusterLock {
    private Jedis jedis;

    private static final String KEY = "dbbus.jedis.";
    private static final String VALUE = "true";
    private String key;

    public RedisClusterLock(ClusterConfig clusterConfig) {
        String[] hostPort = clusterConfig.getHostPort().split("\\:");
        this.jedis = new Jedis(hostPort[0], Integer.parseInt(hostPort[1]));
        this.key = KEY + Integer.toHexString(this.hashCode());
        this.unLock();
    }

    @Override
    public void lock() {
        while (true) {
            if (this.tryLock()) {
                return;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public boolean tryLock() {
        long l = this.jedis.setnx(this.key, VALUE);
        return l == 1;
    }

    @Override
    public void unLock() {
        this.jedis.del(this.key);
    }

}
