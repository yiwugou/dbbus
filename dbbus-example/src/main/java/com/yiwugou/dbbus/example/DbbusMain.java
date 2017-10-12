package com.yiwugou.dbbus.example;

import java.util.HashMap;
import java.util.Map;

import com.yiwugou.dbbus.core.BeanCreater;
import com.yiwugou.dbbus.core.consumer.EventConsumer;
import com.yiwugou.dbbus.core.start.ArgApplication;

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
        Map<String, EventConsumer> eventConsumerMap = new HashMap<>();
        eventConsumerMap.put("t_person", new ExampleEventConsumer());
        new ArgApplication(args, BeanCreater.builder().eventConsumerMap(eventConsumerMap).build()).start();
    }
}
