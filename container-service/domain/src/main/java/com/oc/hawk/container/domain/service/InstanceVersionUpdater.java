package com.oc.hawk.container.domain.service;

import com.oc.hawk.api.exception.DomainNotFoundException;
import com.oc.hawk.container.domain.facade.ProjectFacade;
import com.oc.hawk.container.domain.model.runtime.build.ProjectBuild;
import com.oc.hawk.container.domain.model.runtime.config.BaseInstanceConfig;
import com.oc.hawk.container.domain.model.runtime.config.InstanceConfig;
import com.oc.hawk.container.domain.model.runtime.config.InstanceConfigRepository;
import com.oc.hawk.container.domain.model.runtime.config.InstanceId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class InstanceVersionUpdater {
    private final ProjectFacade projectFacade;
    private final InstanceConfigRepository instanceConfigRepository;


    public InstanceConfig update(InstanceId instanceId, Long projectBuildId) {

        log.info("Update instance {} version from job {}", instanceId, projectBuildId);
        final InstanceConfig config = instanceConfigRepository.byId(instanceId);
        if (config == null) {
            throw new DomainNotFoundException(instanceId.getId());
        }
        final BaseInstanceConfig baseConfig = (BaseInstanceConfig) config.getBaseConfig();

        final ProjectBuild projectBuild = projectFacade.getProjectBuild(projectBuildId);
        baseConfig.updateNewVersion(projectBuild.getVersion());
        instanceConfigRepository.save(config);
        return config;
    }
}
