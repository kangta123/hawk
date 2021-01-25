package com.oc.hawk.container.domain.service;

import com.oc.hawk.api.exception.DomainNotFoundException;
import com.oc.hawk.container.domain.facade.InfrastructureLifeCycleFacade;
import com.oc.hawk.container.domain.model.project.ProjectRuntimeConfig;
import com.oc.hawk.container.domain.model.project.ProjectRuntimeConfigRepository;
import com.oc.hawk.container.domain.model.runtime.config.InstanceConfig;
import com.oc.hawk.container.domain.model.runtime.config.InstanceConfigRepository;
import com.oc.hawk.container.domain.model.runtime.config.InstanceId;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class InstanceStartExecutor {
    private final InfrastructureLifeCycleFacade infrastructureLifeCycleFacade;

    private final InstanceConfigRepository instanceConfigRepository;


    private final ProjectRuntimeConfigRepository projectRuntimeConfigRepository;

    public void start( InstanceConfig config) {
        ProjectRuntimeConfig runtimeConfig = getProjectRuntimeConfig(config);
        infrastructureLifeCycleFacade.start(config, runtimeConfig);
    }
    public void start(InstanceId instanceId) {
        InstanceConfig config = instanceConfigRepository.byId(instanceId);
        if(config == null){
            throw new DomainNotFoundException(instanceId.getId());
        }
        this.start(config);
    }

    private ProjectRuntimeConfig getProjectRuntimeConfig(InstanceConfig config) {
        List<ProjectRuntimeConfig> runtimeConfigs = projectRuntimeConfigRepository.findBy(config.getRuntimeType());
        if (runtimeConfigs != null) {
            return runtimeConfigs.iterator().next();
        }
        return null;
    }
}
