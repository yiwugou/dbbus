package com.yiwugou.dbbus.core.test;

import com.yiwugou.dbbus.core.BeanCreater;
import com.yiwugou.dbbus.core.start.Application;

public class CoreTest {

    public static void main(String[] args) throws Exception {
        new Application(args, BeanCreater.builder().eventConsumer(new TestEventConsumer()).build()).start();
    }
}
