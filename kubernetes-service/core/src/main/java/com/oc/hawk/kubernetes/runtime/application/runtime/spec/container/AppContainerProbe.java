package com.oc.hawk.kubernetes.runtime.application.runtime.spec.container;

import com.oc.hawk.container.domain.config.HealthCheckProperties;
import com.oc.hawk.container.domain.model.runtime.info.RuntimeHealthCheck;
import com.oc.hawk.kubernetes.runtime.application.runtime.spec.NetworkConfigSpec;
import com.oc.hawk.kubernetes.runtime.application.runtime.spec.RuntimeConfigSpec;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.Probe;
import io.fabric8.kubernetes.api.model.ProbeBuilder;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@RequiredArgsConstructor
public class AppContainerProbe {
    public static final String HTTP_PROTOCOL = "HTTP";
    private final Container appContainer;

    public void configContainerProbe(RuntimeHealthCheck healthCheck) {
        appContainer.setLivenessProbe(getLivenessProbe(healthCheck));
        appContainer.setReadinessProbe(getHttpReadinessProbe(healthCheck));
    }

    private Probe getHttpReadinessProbe(RuntimeHealthCheck healthCheck) {
        if (!healthCheck.isReadinessEnabled()) {
            return null;
        }
        HealthCheckProperties healthCheckProperties = healthCheck.getHealthCheckProperties();

        return new ProbeBuilder()
            .withFailureThreshold(healthCheckProperties.getReadinessProbeFailureThreshold())
            .withInitialDelaySeconds(healthCheckProperties.getReadinessProbeInitialDelaySeconds())
            .withPeriodSeconds(healthCheckProperties.getReadinessProbePeriodSeconds())
            .withSuccessThreshold(healthCheckProperties.getReadinessProbeSuccessThreshold())
            .withNewHttpGet()
            .withScheme(HTTP_PROTOCOL)
            .withNewPort(healthCheck.getPort())
            .withNewPath(healthCheck.getPath())
            .endHttpGet()
            .withTimeoutSeconds(healthCheckProperties.getReadinessProbeTimeoutSeconds())
            .build();
    }

    private Probe getLivenessProbe(RuntimeHealthCheck healthCheck) {
        if (!healthCheck.isLivenessEnabled()) {
            return null;
        }
        HealthCheckProperties healthCheckProperties = healthCheck.getHealthCheckProperties();
        return new ProbeBuilder()
            .withFailureThreshold(healthCheckProperties.getLivenessProbeFailureThreshold())
            .withInitialDelaySeconds(healthCheckProperties.getLivenessProbeInitialDelaySeconds())
            .withPeriodSeconds(healthCheckProperties.getLivenessProbePeriodSeconds())
            .withSuccessThreshold(healthCheckProperties.getLivenessProbeSuccessThreshold())
            .withNewTcpSocket()
            .withNewPort(healthCheck.getPort())
            .endTcpSocket()
            .withTimeoutSeconds(healthCheckProperties.getLivenessProbeTimeoutSeconds())
            .build();
    }
}
