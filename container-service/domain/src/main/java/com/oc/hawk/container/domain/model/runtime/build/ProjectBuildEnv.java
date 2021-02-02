package com.oc.hawk.container.domain.model.runtime.build;

import com.oc.hawk.container.domain.config.ContainerConfiguration;
import com.oc.hawk.container.domain.config.ContainerRuntimeConfig;

import java.util.Map;

/**
 * @author kangta123
 */
public enum ProjectBuildEnv {

    BASE_IMAGE("BASE_IMAGE"), IMAGE_PREFIX("IMAGE_PREFIX");
    private final String env;

    ProjectBuildEnv(String env) {
        this.env = env;
    }

    public static Map<String, Object> map(ContainerRuntimeConfig.ContainerConfig config, ContainerConfiguration containerConfiguration) {
        return Map.of(BASE_IMAGE.env, config.getDataImage(),
            IMAGE_PREFIX.env,
            containerConfiguration.getAppImagePrefix());
    }
}
