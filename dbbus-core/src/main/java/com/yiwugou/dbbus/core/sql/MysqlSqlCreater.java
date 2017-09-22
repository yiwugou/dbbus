package com.yiwugou.dbbus.core.sql;

/**
 *
 * MysqlSqlCreater
 *
 * @author zhanxiaoyong@yiwugou.com
 *
 * @since 2017年9月22日 上午9:19:47
 */
public class MysqlSqlCreater extends AbstractSqlCreater implements SqlCreater {
    @Override
    public String getEventLimitSql() {
        return "select txn, table_name as tableName, id, action, status, ts from DBBUS_EVENT where status=? order by txn asc limit ?";
    }

}
