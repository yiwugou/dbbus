package com.yiwugou.dbbus.core.test;

import com.yiwugou.dbbus.core.BeanCreater;
import com.yiwugou.dbbus.core.start.Application;

/**
 *
 * CoreTest
 *
 * @author zhanxiaoyong@yiwugou.com
 *
 * @since 2017年9月20日 上午8:58:14
 */
public class CoreTest {

    public static void main(String[] args) throws Exception {
        new Application(args, BeanCreater.builder().eventConsumer(new TestEventConsumer()).build()).start();
    }
}
