package com.yiwugou.dbbus.core;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * TableId
 *
 * @author zhanxiaoyong@yiwugou.com
 *
 * @since 2017年9月20日 上午8:55:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableId implements Serializable {
    private static final long serialVersionUID = 1L;
    private String tableName;
    private String id;
}
