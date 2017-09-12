package com.yiwugou.dbbus.example;

import com.yiwugou.dbbus.core.BeanCreater;
import com.yiwugou.dbbus.core.start.DbbusStart;

public class DbbusMain {
    public static void main(String[] args) throws Exception {
        new DbbusStart(args, BeanCreater.builder().eventConsumer(new ExampleEventConsumer()).build()).start();
    }
}
