package com.oc.hawk.container.domain.service.decorator;

import com.google.common.base.Objects;
import com.oc.hawk.container.domain.config.ContainerConfiguration;
import com.oc.hawk.container.domain.model.runtime.config.BaseInstanceConfig;
import com.oc.hawk.container.domain.model.runtime.config.InstanceConfig;

public class InstanceLogDecorator extends AbstractInstanceConfigDecorator {
    ContainerConfiguration containerConfiguration;

    public InstanceLogDecorator(InstanceConfigRuntimeDecorator configRuntimeDecorator, ContainerConfiguration configuration) {
        super(configRuntimeDecorator);
        this.containerConfiguration = configuration;
    }

    @Override
    public void config(InstanceConfig instanceConfig) {
        super.config(instanceConfig);

        if (Objects.equal(containerConfiguration.getPvcLog(), Boolean.TRUE)) {
            BaseInstanceConfig baseConfig = (BaseInstanceConfig) instanceConfig.getBaseConfig();
            baseConfig.addVolume(baseConfig.getLog().getLogVolume(baseConfig.getNetwork().getServiceName()));
        }
    }
}
