package com.yiwugou.dbbus.core.start;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yiwugou.dbbus.core.BeanCreater;
import com.yiwugou.dbbus.core.DbbusEvent;
import com.yiwugou.dbbus.core.DbbusException;
import com.yiwugou.dbbus.core.config.Command;
import com.yiwugou.dbbus.core.config.Config;
import com.yiwugou.dbbus.core.config.Constants;
import com.yiwugou.dbbus.core.jdbc.JdbcTemplate;
import com.yiwugou.dbbus.core.task.EventPullerTask;
import com.yiwugou.dbbus.core.util.CommonUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 *
 * ArgApplication
 *
 * @author zhanxiaoyong@yiwugou.com
 *
 * @since 2017年10月16日 下午4:25:52
 */
@Accessors(chain = true)
public class ArgApplication implements Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public ArgApplication(String[] args, BeanCreater beanCreater) {
        this.beanCreater = beanCreater;
        this.initConfig(args);
        this.initEventQueue();
    }

    public ArgApplication(String[] args) {
        this(args, null);
    }

    private EventPullerTask eventPullerTask;

    @Getter
    private Config config;

    @Setter
    @Getter
    private BeanCreater beanCreater;

    @Getter
    private Properties properties;

    @Getter
    private BlockingQueue<DbbusEvent> beforeMergeQueue;

    @Getter
    private BlockingQueue<DbbusEvent> afterMergeQueue;

    private boolean started = false;

    @Override
    public JdbcTemplate getJdbcTemplate() {
        return this.eventPullerTask.getJdbcTemplate();
    }

    private void initConfig(String[] args) {
        try {
            Command command = Command.parse(args);
            this.properties = new Properties();
            this.properties.load(Application.class.getClassLoader().getResourceAsStream(Constants.CONFIG_FILE));
            if (CommonUtils.isNotBlank(command.getConfigFile())) {
                File file = new File(command.getConfigFile());
                if (file.exists()) {
                    Properties p = new Properties();
                    p.load(new FileInputStream(command.getConfigFile()));
                    this.properties = CommonUtils.mergeProperties(this.properties, p);
                }
            }

            this.config = Config.initConfig(this.properties);
        } catch (Exception e) {
            throw new DbbusException(e);
        }
    }

    private void initEventQueue() {
        this.beforeMergeQueue = new LinkedBlockingQueue<>(this.config.getEventConfig().getQueueCapacity());
        this.afterMergeQueue = new LinkedBlockingQueue<>(this.config.getEventConfig().getQueueCapacity());
    }

    private void initPullerTask() {
        this.eventPullerTask = new EventPullerTask(this);
    }

    @Override
    public synchronized void start() {
        if (!this.started) {
            this.initPullerTask();
            this.eventPullerTask.execute();
            logger.info("started---" + this.toString());
            this.started = true;
        }
        logger.info("application has started");
    }

}
