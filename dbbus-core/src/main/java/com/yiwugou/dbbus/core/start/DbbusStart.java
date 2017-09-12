package com.yiwugou.dbbus.core.start;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yiwugou.dbbus.core.BeanCreater;
import com.yiwugou.dbbus.core.DataContainer;
import com.yiwugou.dbbus.core.config.Command;
import com.yiwugou.dbbus.core.config.Config;
import com.yiwugou.dbbus.core.config.Constants;
import com.yiwugou.dbbus.core.task.EventPullerTask;
import com.yiwugou.dbbus.core.util.CommonUtils;

import lombok.Setter;
import lombok.ToString;

@ToString
public class DbbusStart {
    private static final Logger logger = LoggerFactory.getLogger(DbbusStart.class);

    private EventPullerTask eventPullerTask;

    private Config config;

    @Setter
    private BeanCreater beanCreater;

    public DbbusStart(String[] args, BeanCreater beanCreater) {
        this.beanCreater = beanCreater;
        this.initConfig(args);
        this.initEventQueue();
        this.initPullerTask();
        logger.info(this.toString());
    }

    public DbbusStart(String[] args) {
        this(args, BeanCreater.builder().build());
    }

    private void initConfig(String[] args) {
        try {
            Command command = Command.parse(args);
            Properties p1 = new Properties();
            p1.load(DbbusStart.class.getClassLoader().getResourceAsStream(Constants.CONFIG_FILE));
            if (CommonUtils.isNotBlank(command.getConfigFile())) {
                File file = new File(command.getConfigFile());
                if (file.exists()) {
                    Properties p2 = new Properties();
                    p2.load(new FileInputStream(command.getConfigFile()));
                    p1 = CommonUtils.mergeProperties(p1, p2);
                }
            }
            config = Config.initConfig(p1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void initEventQueue() {
        DataContainer.initEventQueue(config.getEventConfig().getQueueCapacity());
    }

    private void initPullerTask() {
        this.eventPullerTask = new EventPullerTask(config, beanCreater);
    }

    public void start() {
        eventPullerTask.execute();
    }

    public static void main(String[] args) throws Exception {
        new DbbusStart(args).start();
    }

}
