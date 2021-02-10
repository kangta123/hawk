package com.oc.hawk.container.domain.model.runtime.info;

import com.oc.hawk.container.domain.config.HealthCheckProperties;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author kangta123
 */
@Getter
public class RuntimeHealthCheck {
    private final String path;
    private final boolean enabled;
    private final int port;
    private final HealthCheckProperties healthCheckProperties;


    public RuntimeHealthCheck(String path, boolean enabled, int port, HealthCheckProperties healthCheckProperties) {

        this.path = path;
        this.enabled = enabled;
        this.port = port;
        this.healthCheckProperties = healthCheckProperties;
    }

    public boolean isReadinessEnabled() {
        return isLivenessEnabled() && StringUtils.isNotEmpty(path);
    }

    public boolean isLivenessEnabled() {
        return enabled;
    }
}
