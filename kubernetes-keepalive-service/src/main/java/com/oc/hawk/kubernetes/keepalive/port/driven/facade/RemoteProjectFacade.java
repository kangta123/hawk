package com.oc.hawk.kubernetes.keepalive.port.driven.facade;

import com.oc.hawk.kubernetes.keepalive.application.facade.ProjectFacade;
import com.oc.hawk.kubernetes.keepalive.port.driven.facade.feign.ProjectGateway;
import com.oc.hawk.project.api.dto.ProjectBuildJobDetailDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RemoteProjectFacade implements ProjectFacade {
    private final ProjectGateway projectGateway;
    @Override
    public ProjectBuildJobDetailDTO getProjectBuildJob(Long jobId) {
        return projectGateway.getProjectBuildJob(jobId);
    }
}
