package com.yiwugou.dbbus.core;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableId implements Serializable {
    private static final long serialVersionUID = 1L;
    private String tableName;
    private String id;
}
