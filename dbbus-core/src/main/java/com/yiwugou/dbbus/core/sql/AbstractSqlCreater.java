package com.yiwugou.dbbus.core.sql;

import com.yiwugou.dbbus.core.enums.Status;

/**
 *
 * AbstractSqlCreater
 *
 * @author zhanxiaoyong@yiwugou.com
 *
 * @since 2017年9月22日 上午9:19:41
 */
public abstract class AbstractSqlCreater implements SqlCreater {
    @Override
    public String getEventUpdateSql(Status status, long txnFrom, long txnTo) {
        return "update DBBUS_EVENT set status=" + status.ordinal() + " where txn>=" + txnFrom + " and txn<=" + txnTo;
    }

    @Override
    public String getOneEventUpdateSql(Status status, long txn) {
        return "update DBBUS_EVENT set status=" + status.ordinal() + " where txn=" + txn;
    }

    @Override
    public String getEventDeleteSql(Status status) {
        return "delete from DBBUS_EVENT where status=" + status.ordinal();
    }

    @Override
    public String getSelectSql(String tableName, String columns, String idName, String idValue) {
        return "select " + columns + " from " + tableName + " where " + idName + "='" + idValue + "'";
    }
}
