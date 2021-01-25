package com.oc.hawk.container.domain.service.decorator;

import com.oc.hawk.container.domain.config.ContainerConfiguration;
import com.oc.hawk.container.domain.model.runtime.config.BaseInstanceConfig;
import com.oc.hawk.container.domain.model.runtime.config.InstanceConfig;

public class NginxDecorator extends AbstractInstanceConfigDecorator {
    private final ContainerConfiguration containerConfiguration;
    private final String ENV_NGINX_GATEWAY_URL = "gateway";


    public NginxDecorator(InstanceConfigRuntimeDecorator configRuntimeDecorator, ContainerConfiguration containerConfiguration) {
        super(configRuntimeDecorator);
        this.containerConfiguration = containerConfiguration;
    }


    @Override
    public void config(InstanceConfig instanceConfig) {
        super.config(instanceConfig);
        BaseInstanceConfig baseConfig = (BaseInstanceConfig) instanceConfig.getBaseConfig();
        baseConfig.addEnv(ENV_NGINX_GATEWAY_URL, containerConfiguration.getGatewayUrl());
    }
}
