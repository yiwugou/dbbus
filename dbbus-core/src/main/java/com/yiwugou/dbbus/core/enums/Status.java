package com.yiwugou.dbbus.core.enums;

/**
 *
 * <pre>
 * Status 顺序别改
 * </pre>
 *
 * @author zhanxiaoyong@yiwugou.com
 *
 * @since 2017年9月8日 上午9:39:30
 */
public enum Status {
    /**
     * 0-unread
     */
    UNREAD,
    /**
     * 1-readed
     */
    READED;

    public static Status parse(Integer status) {
        for (Status a : Status.values()) {
            if (Integer.valueOf(a.ordinal()).equals(status)) {
                return a;
            }
        }
        throw new IllegalArgumentException("no status suppored, status=" + status);
    }
}
