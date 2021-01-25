package com.oc.hawk.container.domain.service.decorator;

import com.oc.hawk.container.domain.model.runtime.config.BaseInstanceConfig;
import com.oc.hawk.container.domain.model.runtime.config.InstanceConfig;
import com.oc.hawk.container.domain.model.runtime.config.JavaInstanceConfig;

import java.util.Objects;

public class JavaRemoteDebugDecorator extends AbstractInstanceConfigDecorator {

    private static final String REMOTE_DEBUG_ENV_KEY = "REMOTE_DEBUG";

    public JavaRemoteDebugDecorator(InstanceConfigRuntimeDecorator configRuntimeDecorator) {
        super(configRuntimeDecorator);
    }

    @Override
    public void config(InstanceConfig instanceConfig) {
        super.config(instanceConfig);

        BaseInstanceConfig baseInstanceConfig = (BaseInstanceConfig) instanceConfig.getBaseConfig();
        if (instanceConfig instanceof JavaInstanceConfig) {
            JavaInstanceConfig javaInstanceConfig = (JavaInstanceConfig) instanceConfig;
            baseInstanceConfig.addEnv(REMOTE_DEBUG_ENV_KEY, Objects.equals(javaInstanceConfig.getDebug(), Boolean.TRUE) ? ENABLED : DISABLED);
        }
    }
}
