package com.oc.hawk.project.domain.model.buildjob;

import com.oc.hawk.ddd.DomainEntity;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@DomainEntity
@Data
@Builder
public class ProjectBuildStage implements Comparable<ProjectBuildStage> {
    private ProjectBuildStageID id;
    private JobStage stage;
    private boolean success;
    private LocalDateTime createdAt;
    private String data;


    @Override
    public int compareTo(ProjectBuildStage o) {
        return this.id.getId() > o.id.getId() ? 1 : -1;
    }
}
