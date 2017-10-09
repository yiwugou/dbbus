package com.yiwugou.dbbus.core.config;

import java.util.Properties;

import com.yiwugou.dbbus.core.DbbusException;
import com.yiwugou.dbbus.core.util.CommonUtils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * JdbcConfig
 *
 * @author zhanxiaoyong@yiwugou.com
 *
 * @since 2017年9月20日 上午8:56:34
 */
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JdbcConfig {
    @Getter
    @Setter(AccessLevel.PRIVATE)
    private String driver;
    @Getter
    @Setter(AccessLevel.PRIVATE)
    private String url;
    @Getter
    @Setter(AccessLevel.PRIVATE)
    private String username;
    @Getter
    @Setter(AccessLevel.PRIVATE)
    private String password;
    @Getter
    @Setter(AccessLevel.PRIVATE)
    private Integer maxActive = 100;
    @Getter
    @Setter(AccessLevel.PRIVATE)
    private Integer minIdle = 10;

    public static JdbcConfig init(Properties properties) {
        JdbcConfig config = new JdbcConfig();

        String driver = properties.getProperty(Constants.JDBC_DRIVER);
        if (CommonUtils.isNotBlank(driver)) {
            config.setDriver(driver);
        } else {
            throw new DbbusException("jdbc driver must not be null");
        }

        String url = properties.getProperty(Constants.JDBC_URL);
        if (CommonUtils.isNotBlank(url)) {
            config.setUrl(url);
        } else {
            throw new DbbusException("jdbc url must not be null");
        }

        String username = properties.getProperty(Constants.JDBC_USERNAME);
        if (CommonUtils.isNotBlank(username)) {
            config.setUsername(username);
        } else {
            throw new DbbusException("jdbc username must not be null");
        }

        String password = properties.getProperty(Constants.JDBC_PASSWORD);
        if (CommonUtils.isNotBlank(password)) {
            config.setPassword(password);
        } else {
            throw new DbbusException("jdbc password must not be null");
        }

        String maxActive = properties.getProperty(Constants.JDBC_MAX_ACTIVE);
        if (CommonUtils.isNotBlank(maxActive)) {
            config.setMaxActive(Integer.parseInt(maxActive));
        }

        String minIdle = properties.getProperty(Constants.JDBC_MIN_IDLE);
        if (CommonUtils.isNotBlank(minIdle)) {
            config.setMinIdle(Integer.parseInt(minIdle));
        }

        return config;
    }

}
