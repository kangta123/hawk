package com.oc.hawk.project.domain.model.buildjob;


import com.oc.hawk.ddd.DomainValueObject;
import lombok.Getter;

@DomainValueObject
@Getter
public class ProjectBuildPost {
    private final Long deployTo;
    private final ProjectBuildJobID projectBuildJobId;
    private final String deployToInstance;

    public ProjectBuildPost(Long deployTo, ProjectBuildJobID projectBuildJobId, String deployToInstance) {
        this.deployTo = deployTo;
        this.projectBuildJobId = projectBuildJobId;
        this.deployToInstance = deployToInstance;
    }
}
