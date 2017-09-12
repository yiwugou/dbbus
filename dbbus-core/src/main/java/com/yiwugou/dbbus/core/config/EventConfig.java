package com.yiwugou.dbbus.core.config;

import java.util.Properties;

import com.yiwugou.dbbus.core.util.CommonUtils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventConfig {
    @Getter
    @Setter(AccessLevel.PRIVATE)
    private Boolean mergeUpdate = true;
    @Getter
    @Setter(AccessLevel.PRIVATE)
    private Integer maxRowNum = 100;
    @Getter
    @Setter(AccessLevel.PRIVATE)
    private Integer queueCapacity = 10000;
    @Getter
    @Setter(AccessLevel.PRIVATE)
    private Integer pullerPoolSize = 10;
    @Getter
    @Setter(AccessLevel.PRIVATE)
    private Long pullerDelay = 1000L;
    @Getter
    @Setter(AccessLevel.PRIVATE)
    private Long clearDelay = -1L;
    @Getter
    @Setter(AccessLevel.PRIVATE)
    private String consumerClass;

    public static EventConfig init(Properties properties) {
        EventConfig config = new EventConfig();
        String mergeUpdate = properties.getProperty(Constants.EVENT_MERGE_UPDATE);
        if (CommonUtils.isNotBlank(mergeUpdate)) {
            config.setMergeUpdate(Boolean.valueOf(mergeUpdate));
        }

        String maxRowNum = properties.getProperty(Constants.EVENT_MAX_ROW_NUM);
        if (CommonUtils.isNotBlank(maxRowNum)) {
            config.setMaxRowNum(Integer.valueOf(maxRowNum));
        }

        String queueCapacity = properties.getProperty(Constants.EVENT_QUEUE_CAPACITY);
        if (CommonUtils.isNotBlank(queueCapacity)) {
            config.setQueueCapacity(Integer.valueOf(queueCapacity));
        }

        String puulerPoolSize = properties.getProperty(Constants.EVENT_PULLER_POOL_SIZE);
        if (CommonUtils.isNotBlank(puulerPoolSize)) {
            config.setPullerPoolSize(Integer.valueOf(puulerPoolSize));
        }

        String pullerDelay = properties.getProperty(Constants.EVENT_PULLER_DELAY);
        if (CommonUtils.isNotBlank(pullerDelay)) {
            config.setPullerDelay(Long.valueOf(pullerDelay));
        }

        String clearDelay = properties.getProperty(Constants.EVENT_CLEAR_DELAY);
        if (CommonUtils.isNotBlank(clearDelay)) {
            config.setClearDelay(Long.valueOf(clearDelay));
        }

        String consumerClass = properties.getProperty(Constants.EVENT_CONSUMER_CLASS);
        if (CommonUtils.isNotBlank(consumerClass)) {
            config.setConsumerClass(consumerClass);
        } else {
            throw new RuntimeException("event consumer class must not be null");
        }
        return config;
    }
}
