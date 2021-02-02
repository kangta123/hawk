package com.oc.hawk.container.domain.service;

import com.oc.hawk.container.domain.model.runtime.config.InstanceConfig;

public interface InstanceConfigDecoratorFacade {
    void decorate(InstanceConfig config);
}
