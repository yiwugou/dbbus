package com.yiwugou.dbbus.core.config;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * Config
 *
 * @author zhanxiaoyong@yiwugou.com
 *
 * @since 2017年9月20日 上午8:56:14
 */
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Config {
    private static final Logger logger = LoggerFactory.getLogger(Config.class);

    @Getter
    @Setter(AccessLevel.PRIVATE)
    private JdbcConfig jdbcConfig;

    @Getter
    @Setter(AccessLevel.PRIVATE)
    private EventConfig eventConfig;

    @Getter
    @Setter(AccessLevel.PRIVATE)
    private ClusterConfig clusterConfig;

    @Getter
    @Setter(AccessLevel.PRIVATE)
    private TableConfig tableConfig;

    public static Config initConfig(Properties properties) {
        Config config = new Config();
        config = new Config();
        config.setEventConfig(EventConfig.init(properties));
        config.setJdbcConfig(JdbcConfig.init(properties));
        config.setClusterConfig(ClusterConfig.init(properties));
        config.setTableConfig(TableConfig.init(properties));
        logger.info("config init " + config);
        return config;
    }
}
