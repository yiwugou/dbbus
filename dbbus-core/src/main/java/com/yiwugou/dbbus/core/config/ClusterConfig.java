package com.yiwugou.dbbus.core.config;

import java.util.Properties;

import com.yiwugou.dbbus.core.enums.ClusterType;
import com.yiwugou.dbbus.core.util.CommonUtils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * ClusterConfig
 *
 * @author zhanxiaoyong@yiwugou.com
 *
 * @since 2017年9月20日 上午8:56:03
 */
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClusterConfig {
    @Getter
    @Setter(AccessLevel.PRIVATE)
    private ClusterType clusterType = ClusterType.NONE;

    @Getter
    @Setter(AccessLevel.PRIVATE)
    private String hostPort;

    public static ClusterConfig init(Properties properties) {
        ClusterConfig config = new ClusterConfig();

        String clusterType = properties.getProperty(Constants.CLUSTER_TYPE);
        if (CommonUtils.isNotBlank(clusterType)) {
            config.setClusterType(ClusterType.parse(clusterType));
        }

        String hostPort = properties.getProperty(Constants.CLUSTER_HOST_PORT);
        if (CommonUtils.isNotBlank(hostPort)) {
            config.setHostPort(hostPort);
        }

        return config;
    }
}
