package com.oc.hawk.project.api.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProjectBuildJobStageDTO {
    private Boolean success;
    private LocalDateTime startTime;
    private String title;
    private String jobStage;
    private String data;
}
