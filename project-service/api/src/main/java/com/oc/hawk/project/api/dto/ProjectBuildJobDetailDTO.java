package com.oc.hawk.project.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProjectBuildJobDetailDTO extends ProjectBuildJobDTO {
    private List<ProjectBuildJobStageDTO> stages;
    private GitCommitRecordDTO record;
}
