package com.yiwugou.dbbus.core.sql;

/**
 *
 * SqlCreater
 *
 * @author zhanxiaoyong@yiwugou.com
 *
 * @since 2017年9月22日 上午9:19:56
 */
public interface SqlCreater {

    String getEventLimitSql();

    String getEventUpdateSql();

    String getOneEventUpdateSql();

    String getEventDeleteSql();

    String getSelectSql(String tableName, String columns, String id);

}
