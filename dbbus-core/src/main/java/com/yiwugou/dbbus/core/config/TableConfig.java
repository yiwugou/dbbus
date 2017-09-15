package com.yiwugou.dbbus.core.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TableConfig {

    /**
     * TableName:IdColumns
     */
    @Getter
    @Setter(AccessLevel.PRIVATE)
    private Map<String, IdColumns> tableMap;

    public IdColumns getIdColumns(String tableName) {
        return this.tableMap.get(tableName);
    }

    public static TableConfig init(Properties properties) {
        Map<String, IdColumns> tableMap = new HashMap<>();
        for (String key : properties.stringPropertyNames()) {
            String tableName = IdColumns.findTableById(key);
            if (tableName != null) {
                IdColumns idColumns = tableMap.get(tableName);
                if (idColumns == null) {
                    idColumns = new IdColumns();
                    idColumns.setTableName(tableName);
                }
                idColumns.setId(properties.getProperty(key));
                tableMap.put(tableName, idColumns);
            }

            tableName = IdColumns.findTableByColumns(key);
            if (tableName != null) {
                IdColumns idColumns = tableMap.get(tableName);
                if (idColumns == null) {
                    idColumns = new IdColumns();
                    idColumns.setTableName(tableName);
                }
                idColumns.setColumns(properties.getProperty(key));
                tableMap.put(tableName, idColumns);
            }
            tableName = IdColumns.findTableByEnable(key);
            if (tableName != null) {
                IdColumns idColumns = tableMap.get(tableName);
                if (idColumns == null) {
                    idColumns = new IdColumns();
                    idColumns.setTableName(tableName);
                }
                idColumns.setEnable(Boolean.valueOf(properties.getProperty(key)));
                tableMap.put(tableName, idColumns);
            }
        }

        TableConfig config = new TableConfig();
        config.setTableMap(tableMap);
        return config;
    }
}
