package com.yiwugou.dbbus.core.sql;

import com.yiwugou.dbbus.core.enums.Status;

/**
 *
 * MysqlSqlCreater
 *
 * @author zhanxiaoyong@yiwugou.com
 *
 * @since 2017年9月22日 上午9:19:47
 */
public class MysqlSqlCreater extends AbstractSqlCreater {

    @Override
    public String getEventLimitSql(Status status, int limit) {
        return "select txn, table_name as tableName, id, action, status, ts from DBBUS_EVENT where status="
                + status.ordinal() + " order by txn asc limit " + limit;
    }

}
