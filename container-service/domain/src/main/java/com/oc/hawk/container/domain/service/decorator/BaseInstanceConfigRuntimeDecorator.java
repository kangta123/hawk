package com.oc.hawk.container.domain.service.decorator;

import com.oc.hawk.container.domain.config.ContainerConfiguration;
import com.oc.hawk.container.domain.model.runtime.config.BaseInstanceConfig;
import com.oc.hawk.container.domain.model.runtime.config.InstanceConfig;
import com.oc.hawk.container.domain.model.runtime.config.InstanceNetwork;
import org.apache.commons.lang3.StringUtils;

public class BaseInstanceConfigRuntimeDecorator extends AbstractInstanceConfigDecorator {
    private final static String ENV_SERVICE_NAME_KEY = "SERVICE_NAME";
    private final static String ENV_SERVER_PORT = "SERVER_PORT";
    private final static String ENV_HOSTS = "HOSTS";
    private final static String ENV_HAWK_GATEWAY = "HAWK_GATEWAY";
    private final ContainerConfiguration containerConfiguration;


    public BaseInstanceConfigRuntimeDecorator(ContainerConfiguration containerConfiguration) {
        super(null);
        this.containerConfiguration = containerConfiguration;
    }

    @Override
    public void config(InstanceConfig instanceConfig) {
        super.config(instanceConfig);

        BaseInstanceConfig baseConfig = (BaseInstanceConfig) instanceConfig.getBaseConfig();

        if (instanceConfig instanceof BaseInstanceConfig) {
            return;
        }

        String serviceName = baseConfig.getNetwork().getServiceName();
        baseConfig.addEnv(ENV_SERVICE_NAME_KEY, serviceName);

        InstanceNetwork network = baseConfig.getNetwork();
        if (network != null) {
            baseConfig.addEnv(ENV_SERVER_PORT, String.valueOf(network.getInnerPort()));
            baseConfig.addEnv(ENV_HOSTS, StringUtils.defaultIfEmpty(baseConfig.getHost().getHosts(), ""));
        }

        baseConfig.addEnv(ENV_HAWK_GATEWAY, containerConfiguration.getGatewayUrl());
    }
}
