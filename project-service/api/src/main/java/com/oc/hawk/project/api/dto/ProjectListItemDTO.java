package com.oc.hawk.project.api.dto;

import lombok.Data;

@Data
public class ProjectListItemDTO {
    private Long id;
    private String name;
    private String descn;
    private String projectType;
    private Long lastBuildTime;
    private String state;
    private Integer runningCount;
    private Integer configCount;
}
