package com.lgl.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 刘国良
 * @Date 2025/5/12 15:08
 * @Destription
 */

@Data
public class KnowledgeCatalog {
    private Integer id;
    private Integer pid;
    private String level;
    private String catalogName;
    private String deptId;
    private String teamId;
    private Integer sort;
    private Integer modifyUserId;
    private java.sql.Timestamp  modifyTime;
    private String deleteFlag;

    private List<KnowledgeCatalog> children = new ArrayList<>();
}

