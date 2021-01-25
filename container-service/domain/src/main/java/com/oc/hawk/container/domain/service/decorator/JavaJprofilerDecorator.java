package com.oc.hawk.container.domain.service.decorator;

import com.oc.hawk.container.domain.model.runtime.config.BaseInstanceConfig;
import com.oc.hawk.container.domain.model.runtime.config.InstanceConfig;
import com.oc.hawk.container.domain.model.runtime.config.JavaInstanceConfig;

import java.util.Objects;

public class JavaJprofilerDecorator extends AbstractInstanceConfigDecorator {

    private static final String JPROFILER_ENV_KEY = "JPROFILER";

    public JavaJprofilerDecorator(InstanceConfigRuntimeDecorator configRuntimeDecorator) {
        super(configRuntimeDecorator);
    }

    @Override
    public void config(InstanceConfig instanceConfig) {
        super.config(instanceConfig);

        BaseInstanceConfig baseInstanceConfig = (BaseInstanceConfig) instanceConfig.getBaseConfig();
        if (instanceConfig instanceof JavaInstanceConfig) {
            JavaInstanceConfig javaInstanceConfig = (JavaInstanceConfig) instanceConfig;
            Boolean jProfiler = javaInstanceConfig.getJprofiler();
            baseInstanceConfig.addEnv(JPROFILER_ENV_KEY, Objects.equals(jProfiler, Boolean.TRUE) ? ENABLED : DISABLED);
        }
    }
}
