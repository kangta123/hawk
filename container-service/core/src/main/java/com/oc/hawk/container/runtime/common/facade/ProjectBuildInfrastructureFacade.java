package com.oc.hawk.container.runtime.common.facade;

import com.oc.hawk.project.api.dto.ProjectBuildReadyDTO;

public interface ProjectBuildInfrastructureFacade {
    void createBuildRuntime(Long domainId, ProjectBuildReadyDTO data);

//    void watchLog(long domainId, CreateRuntimeInfoSpecCommand spec);

    void stopBuildRuntime(Long domainId);

}
