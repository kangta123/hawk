package com.oc.hawk.kubernetes.keepalive.application.facade;

import com.oc.hawk.project.api.dto.ProjectBuildJobDetailDTO;

public interface ProjectFacade {
    ProjectBuildJobDetailDTO getProjectBuildJob(Long jobId);
}
