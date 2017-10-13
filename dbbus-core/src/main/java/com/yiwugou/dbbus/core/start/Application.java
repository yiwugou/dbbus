package com.yiwugou.dbbus.core.start;

import java.util.Properties;
import java.util.concurrent.BlockingQueue;

import com.yiwugou.dbbus.core.BeanCreater;
import com.yiwugou.dbbus.core.DbbusEvent;
import com.yiwugou.dbbus.core.config.Config;
import com.yiwugou.dbbus.core.jdbc.JdbcTemplate;

/**
 *
 * Application
 *
 * @author zhanxiaoyong@yiwugou.com
 *
 * @since 2017年9月20日 上午8:57:35
 */

public interface Application {
    BeanCreater getBeanCreater();

    Application setBeanCreater(BeanCreater beanCreater);

    Config getConfig();

    BlockingQueue<DbbusEvent> getBeforeMergeQueue();

    BlockingQueue<DbbusEvent> getAfterMergeQueue();

    JdbcTemplate getJdbcTemplate();

    Properties getProperties();

    void start();

}
