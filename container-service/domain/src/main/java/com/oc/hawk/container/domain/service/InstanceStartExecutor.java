package com.oc.hawk.container.domain.service;

import com.oc.hawk.api.exception.DomainNotFoundException;
import com.oc.hawk.container.domain.facade.InfrastructureLifeCycleFacade;
import com.oc.hawk.container.domain.model.runtime.config.InstanceConfig;
import com.oc.hawk.container.domain.model.runtime.config.InstanceConfigRepository;
import com.oc.hawk.container.domain.model.runtime.config.InstanceId;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InstanceStartExecutor {
    private final InfrastructureLifeCycleFacade infrastructureLifeCycleFacade;

    private final InstanceConfigRepository instanceConfigRepository;

    public void start(InstanceConfig config) {
        infrastructureLifeCycleFacade.start(config);
    }

    public void start(InstanceId instanceId) {
        InstanceConfig config = instanceConfigRepository.byId(instanceId);
        if (config == null) {
            throw new DomainNotFoundException(instanceId.getId());
        }
        this.start(config);
    }
}

