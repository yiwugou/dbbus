package com.yiwugou.dbbus.core.sql;

import com.yiwugou.dbbus.core.enums.Status;

/**
 *
 * SqlCreater
 *
 * @author zhanxiaoyong@yiwugou.com
 *
 * @since 2017年9月22日 上午9:19:56
 */
public interface SqlCreater {
    String getSelectSql(String tableName, String columns, String idName, String idValue);

    String getEventDeleteSql(Status status);

    String getOneEventUpdateSql(Status status, long txn);

    String getEventUpdateSql(Status status, long txnFrom, long txnTo);

    String getEventLimitSql(Status status, int limit);

}
