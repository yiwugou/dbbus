package com.yiwugou.dbbus.core.sql;

/**
 *
 * OracleSqlCreater
 *
 * @author zhanxiaoyong@yiwugou.com
 *
 * @since 2017年9月22日 上午9:19:51
 */
public class OracleSqlCreater extends AbstractSqlCreater implements SqlCreater {
    @Override
    public String getEventLimitSql() {
        return "select * from (select txn, table_name as tableName, id, action, status, ts from DBBUS_EVENT where status=? order by txn asc) where rownum<=?";
    }

}
