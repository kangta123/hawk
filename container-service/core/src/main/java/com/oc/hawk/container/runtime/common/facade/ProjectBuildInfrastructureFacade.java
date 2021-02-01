package com.oc.hawk.container.runtime.common.facade;

import com.oc.hawk.container.api.command.CreateRuntimeInfoSpecCommand;
import com.oc.hawk.container.domain.model.project.ProjectRuntimeConfig;
import com.oc.hawk.project.api.dto.ProjectBuildStartDTO;

public interface ProjectBuildInfrastructureFacade {
    void createBuildRuntime(Long domainId, ProjectBuildStartDTO data, ProjectRuntimeConfig projectRuntimeConfig);

//    void watchLog(long domainId, CreateRuntimeInfoSpecCommand spec);

    void stopBuildRuntime(Long domainId);

}
