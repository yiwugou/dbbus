package com.yiwugou.dbbus.core.enums;

/**
 *
 * <pre>
 * Action 顺序别改
 * </pre>
 *
 * @author zhanxiaoyong@yiwugou.com
 *
 * @since 2017年9月8日 上午9:39:18
 */
public enum Action {
    /**
     * 0-insert
     */
    INSERT,
    /**
     * 1-update
     */
    UPDATE,
    /**
     * 2-delete
     */
    DELETE;

    public static Action parse(Integer action) {
        for (Action a : Action.values()) {
            if (Integer.valueOf(a.ordinal()).equals(action)) {
                return a;
            }
        }
        throw new IllegalArgumentException("no action suppored, action=" + action);
    }
}
