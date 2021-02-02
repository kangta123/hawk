package com.oc.hawk.container.domain.service.decorator;

import com.oc.hawk.container.domain.model.runtime.config.BaseInstanceConfig;
import com.oc.hawk.container.domain.model.runtime.config.InstanceConfig;
import com.oc.hawk.container.domain.model.runtime.config.JavaInstanceConfig;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author kangta123
 */
public class JavaPropertyDecorator extends AbstractInstanceConfigDecorator {
    private final static String ENV_JAVA_PROPS = "JAVA_PROPS";

    public JavaPropertyDecorator(InstanceConfigRuntimeDecorator configRuntimeDecorator) {
        super(configRuntimeDecorator);
    }


    @Override
    public void config(InstanceConfig instanceConfig) {
        super.config(instanceConfig);

        BaseInstanceConfig baseConfig = (BaseInstanceConfig) instanceConfig.getBaseConfig();
        if (instanceConfig instanceof JavaInstanceConfig) {
            JavaInstanceConfig javaInstanceConfig = (JavaInstanceConfig) instanceConfig;
            Map<String, String> property = javaInstanceConfig.getJavaConfigedProperty(baseConfig.getName().getName());

            String javaPropertyStr = property.entrySet().stream().map(entry -> "-D" + entry.getKey() + "=" + entry.getValue()).collect(Collectors.joining(" "));
            baseConfig.addEnv(ENV_JAVA_PROPS, "\"" + javaPropertyStr + "\"");
        }

    }

}
