package com.yiwugou.dbbus.core.sql;

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
    public String getEventUpdateSql() {
        return "update DBBUS_EVENT set status=? where txn>=? and txn<=?";
    }

    @Override
    public String getOneEventUpdateSql() {
        return "update DBBUS_EVENT set status=? where txn=?";
    }

    @Override
    public String getEventDeleteSql() {
        return "delete from DBBUS_EVENT where status=?";
    }
}
