package com.oc.hawk.container.domain.service.decorator;

import com.oc.hawk.container.domain.model.runtime.config.BaseInstanceConfig;
import com.oc.hawk.container.domain.model.runtime.config.InstanceConfig;
import com.oc.hawk.container.domain.model.runtime.config.JavaInstanceConfig;

public class EurekaInstanceConfigDecorator extends AbstractInstanceConfigDecorator {
    private static final String ENV_EUREKA_PUBLIC_HOST_KEY = "EUREKA_INSTANCE_HOSTNAME";
    private static final String ENV_EUREKA_PUBLIC_IP_KEY = "EUREKA_INSTANCE_IP_ADDRESS";
    private static final String ENV_EUREKA_PUBLIC_PORT_KEY = "EUREKA_INSTANCE_NONSECUREPORT";
    private static final String ENV_EUREKA_PREFERIP_ADDR_KEY = "EUREKA_INSTANCE_PREFERIPADDRESS";

    public EurekaInstanceConfigDecorator(InstanceConfigRuntimeDecorator configRuntimeDecorator) {
        super(configRuntimeDecorator);
    }

    @Override
    public void config(InstanceConfig instanceConfig) {
        super.config(instanceConfig);

        BaseInstanceConfig baseConfig = (BaseInstanceConfig) instanceConfig.getBaseConfig();
        if (!(instanceConfig instanceof JavaInstanceConfig)) {
            return;
        }

        baseConfig.addEnv(ENV_EUREKA_PUBLIC_PORT_KEY, "8080");
        baseConfig.addEnv(ENV_EUREKA_PUBLIC_HOST_KEY, baseConfig.getNetwork().getServiceName());
        baseConfig.addEnv(ENV_EUREKA_PUBLIC_HOST_KEY, baseConfig.getNetwork().getServiceName());
        baseConfig.addEnv(ENV_EUREKA_PUBLIC_IP_KEY, baseConfig.getNetwork().getServiceName());
        baseConfig.addEnv(ENV_EUREKA_PREFERIP_ADDR_KEY, "true");

    }
}
