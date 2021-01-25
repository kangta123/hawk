package com.oc.hawk.container.domain.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
public class HealthCheckProperties {
    private int livenessProbeFailureThreshold = 2;
    private int livenessProbeInitialDelaySeconds = 300;
    private int livenessProbePeriodSeconds = 10;
    private int livenessProbeSuccessThreshold = 1;
    private int livenessProbeTimeoutSeconds = 1;

    private int readinessProbeFailureThreshold = 1;
    private int readinessProbeInitialDelaySeconds = 10;
    private int readinessProbePeriodSeconds = 5;
    private int readinessProbeSuccessThreshold = 1;
    private int readinessProbeTimeoutSeconds = 1;


}

