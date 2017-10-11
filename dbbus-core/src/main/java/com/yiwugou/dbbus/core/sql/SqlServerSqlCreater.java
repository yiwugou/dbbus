package com.yiwugou.dbbus.core.sql;

import com.yiwugou.dbbus.core.enums.Status;

/**
 *
 * SqlServerSqlCreater
 *
 * @author zhanxiaoyong@yiwugou.com
 *
 * @since 2017年10月11日 下午4:15:15
 */
public class SqlServerSqlCreater extends AbstractSqlCreater {
    @Override
    public String getEventLimitSql(Status status, int limit) {
        return "select top(" + limit
                + ") txn, table_name as tableName, id, action, status, ts from DBBUS_EVENT where status="
                + status.ordinal() + " order by txn desc ";
    }

}
