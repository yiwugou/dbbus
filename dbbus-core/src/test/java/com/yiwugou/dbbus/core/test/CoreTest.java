package com.yiwugou.dbbus.core.test;

import java.util.HashMap;
import java.util.Map;

import com.yiwugou.dbbus.core.BeanCreater;
import com.yiwugou.dbbus.core.consumer.EventConsumer;
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
        Map<String, EventConsumer> eventConsumerMap = new HashMap<>();
        eventConsumerMap.put("t_person", new PersonEventConsumer());
        new Application(args, BeanCreater.builder().defaultEventConsumer(new DefaultEventConsumer())
                .eventConsumerMap(eventConsumerMap).build()).start();
    }
}
