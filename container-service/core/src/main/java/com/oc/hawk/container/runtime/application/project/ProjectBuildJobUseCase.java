package com.oc.hawk.container.runtime.application.project;

import com.oc.hawk.container.domain.facade.ProjectFacade;
import com.oc.hawk.container.domain.model.runtime.build.ProjectBuild;
import com.oc.hawk.container.domain.model.runtime.build.ProjectBuildPost;
import com.oc.hawk.container.domain.model.runtime.config.InstanceId;
import com.oc.hawk.container.runtime.application.instance.InstanceConfigUseCase;
import com.oc.hawk.container.runtime.common.facade.ProjectBuildInfrastructureFacade;
import com.oc.hawk.project.api.dto.ProjectBuildReadyDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProjectBuildJobUseCase {
    private final ProjectBuildInfrastructureFacade projectBuildInfrastructureFacade;
    private final InstanceConfigUseCase instanceConfigUseCase;
    private final ProjectFacade projectFacade;

    public void startBuildJob(Long domainId, ProjectBuildReadyDTO data) {
        log.info("start build job with id {}", domainId);
        projectBuildInfrastructureFacade.createBuildRuntime(domainId, data);
    }

    public void endBuildJob(Long projectBuildJobId) {
        log.info("Build job end with {}", projectBuildJobId);
        final ProjectBuild projectBuild = projectFacade.getProjectBuild(projectBuildJobId);
        if (projectBuild == null) {
            return;
        }

        projectBuildInfrastructureFacade.stopBuildRuntime(projectBuildJobId);
        final ProjectBuildPost projectBuildPost = projectBuild.getPost();
        if (projectBuildPost != null) {
            final InstanceId instanceId = projectBuildPost.getInstanceId();
            instanceConfigUseCase.updateInstanceVersionAndRestart(instanceId, projectBuildJobId);
        }
    }
}
