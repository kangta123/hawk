package com.oc.hawk.project.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectBuildReadyDTO {
    private Map<String, Object> env;
    private String buildType;
    private String runtimeType;
    private String buildOutPut;
    private String projectName;
    private Long jobId;
    private Long projectId;
}
