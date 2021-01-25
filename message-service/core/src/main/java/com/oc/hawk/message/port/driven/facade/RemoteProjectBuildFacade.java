package com.oc.hawk.message.port.driven.facade;

import com.oc.hawk.message.domain.facade.ProjectBuildFacade;
import com.oc.hawk.message.domain.model.ProjectBuildCreator;
import com.oc.hawk.message.port.driven.facade.feign.ProjectBuildGateway;
import com.oc.hawk.project.api.dto.ProjectBuildJobDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RemoteProjectBuildFacade implements ProjectBuildFacade {
    private final ProjectBuildGateway gateway;
    @Override
    public ProjectBuildCreator getBuildJobCreator(Long jobId) {
        final ProjectBuildJobDTO buildJob = gateway.getProjectBuildJob(jobId);
        return new ProjectBuildCreator(buildJob.getCreator(), buildJob.getCreatorName());
    }
}
