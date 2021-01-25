package com.oc.hawk.project.api.dto;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProjectDetailDTO {
    private Long id;
    private String name;
    private String url;
    private String descn;
    private Long departmentId;
    private String projectType;
    private LocalDateTime lastBuildTime;
    private String buildCommand;
    private String buildType;
}
