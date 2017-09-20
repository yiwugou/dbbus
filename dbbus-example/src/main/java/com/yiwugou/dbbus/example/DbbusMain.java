package com.yiwugou.dbbus.example;

import com.yiwugou.dbbus.core.BeanCreater;
import com.yiwugou.dbbus.core.start.Application;

/**
 *
 * DbbusMain
 *
 * @author zhanxiaoyong@yiwugou.com
 *
 * @since 2017年9月20日 上午9:02:37
 */
public class DbbusMain {
    public static void main(String[] args) throws Exception {
        new Application(args, BeanCreater.builder().eventConsumer(new ExampleEventConsumer()).build()).start();
    }
}
