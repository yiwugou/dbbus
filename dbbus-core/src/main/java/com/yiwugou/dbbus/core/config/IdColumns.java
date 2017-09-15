package com.yiwugou.dbbus.core.config;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Data;

@Data
public class IdColumns {
    private String tableName;
    private String id = "id";
    private String columns = "*";
    private Boolean enable = false;

    private static final Pattern P_ID = Pattern.compile(Constants.TABLE_ID);
    private static final Pattern P_COLUMNS = Pattern.compile(Constants.TABLE_COLUMNS);
    private static final Pattern P_ENABLE = Pattern.compile(Constants.TABLE_ENABLE);

    public static String findTableById(String key) {
        Matcher m = P_ID.matcher(key);
        if (m.find()) {
            return m.group(1);
        }
        return null;
    }

    public static String findTableByColumns(String key) {
        Matcher m = P_COLUMNS.matcher(key);
        if (m.find()) {
            return m.group(1);
        }
        return null;
    }

    public static String findTableByEnable(String key) {
        Matcher m = P_ENABLE.matcher(key);
        if (m.find()) {
            return m.group(1);
        }
        return null;
    }
}
