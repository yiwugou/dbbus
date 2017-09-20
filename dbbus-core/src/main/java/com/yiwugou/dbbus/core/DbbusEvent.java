package com.yiwugou.dbbus.core;

import java.io.Serializable;

import com.yiwugou.dbbus.core.enums.Action;
import com.yiwugou.dbbus.core.enums.Status;

import lombok.Data;

/**
 *
 * DbbusEvent
 *
 * @author zhanxiaoyong@yiwugou.com
 *
 * @since 2017年9月20日 上午8:55:16
 */
@Data
public class DbbusEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long txn;
    private String tableName;
    private String id;
    /**
     * 0-insert;1-update;2-delete;
     */
    private Action action;
    /**
     * 0-unread;1-readed;2-error;
     */
    private Status status;

    private Long ts;

}
