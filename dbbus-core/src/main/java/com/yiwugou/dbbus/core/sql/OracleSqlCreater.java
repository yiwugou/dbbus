package com.yiwugou.dbbus.core.sql;

import com.yiwugou.dbbus.core.enums.Status;

/**
 *
 * OracleSqlCreater
 *
 * @author zhanxiaoyong@yiwugou.com
 *
 * @since 2017年9月22日 上午9:19:51
 */
public class OracleSqlCreater extends AbstractSqlCreater {
    @Override
    public String getEventLimitSql(Status status, int limit) {
        return "select txn, table_name as tableName, id, action, status, ts from DBBUS_EVENT where status="
                + status.ordinal() + " and rownum<=" + limit + " order by txn asc";
    }

}
