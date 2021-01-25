package com.oc.hawk.container.domain.service.decorator;

import com.oc.hawk.container.domain.model.runtime.config.BaseInstanceConfig;
import com.oc.hawk.container.domain.model.runtime.config.InstanceConfig;
import com.oc.hawk.container.domain.model.runtime.config.SpringBootInstanceConfig;

public class SpringBootDecorator extends AbstractInstanceConfigDecorator {

    private static final String SPRINGBOOT_PROFILE_ENV_KEY = "SPRING_PROFILES_ACTIVE";

    public SpringBootDecorator(InstanceConfigRuntimeDecorator configRuntimeDecorator) {
        super(configRuntimeDecorator);
    }


    @Override
    public void config(InstanceConfig instanceConfig) {
        super.config(instanceConfig);

        BaseInstanceConfig baseConfig = (BaseInstanceConfig) instanceConfig.getBaseConfig();

        if (instanceConfig instanceof SpringBootInstanceConfig) {
            SpringBootInstanceConfig springBootInstanceConfig = (SpringBootInstanceConfig) instanceConfig;
            baseConfig.addEnv(SPRINGBOOT_PROFILE_ENV_KEY, springBootInstanceConfig.getProfile());
        }

    }

}

