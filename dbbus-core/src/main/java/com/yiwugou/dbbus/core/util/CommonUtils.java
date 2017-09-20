package com.yiwugou.dbbus.core.util;

import java.util.Properties;

/**
 *
 * CommonUtils
 *
 * @author zhanxiaoyong@yiwugou.com
 *
 * @since 2017年9月20日 上午8:58:07
 */
public class CommonUtils {
    public static final boolean isBlank(String str) {
        return str == null || "".equals(str.trim());
    }

    public static final boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static void close(AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Properties mergeProperties(Properties src, Properties desc) {
        Properties properties = new Properties();
        for (String key : src.stringPropertyNames()) {
            properties.setProperty(key, src.getProperty(key));
        }
        for (String key : desc.stringPropertyNames()) {
            properties.setProperty(key, desc.getProperty(key));
        }
        return properties;
    }
}
