package com.oc.hawk.kubernetes.runtime.application.runtime.spec.container;

import com.oc.hawk.container.domain.config.HealthCheckProperties;
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

    public void configContainerProbe(RuntimeConfigSpec configuration) {
        appContainer.setLivenessProbe(getLivenessProbe(configuration));
        if (StringUtils.isNotEmpty(configuration.getHealthCheckPath())) {
            appContainer.setReadinessProbe(getHttpReadinessProbe(configuration));
        }
    }

    private Probe getHttpReadinessProbe(RuntimeConfigSpec configuration) {
        NetworkConfigSpec networkConfigSpec = configuration.getNetworkConfigSpec();
        HealthCheckProperties healthCheckProperties = configuration.getHealthCheckProperties();

        return new ProbeBuilder()
            .withFailureThreshold(healthCheckProperties.getReadinessProbeFailureThreshold())
            .withInitialDelaySeconds(healthCheckProperties.getReadinessProbeInitialDelaySeconds())
            .withPeriodSeconds(healthCheckProperties.getReadinessProbePeriodSeconds())
            .withSuccessThreshold(healthCheckProperties.getReadinessProbeSuccessThreshold())
            .withNewHttpGet()
            .withScheme(HTTP_PROTOCOL)
            .withNewPort(networkConfigSpec.getInnerPort())
            .withNewPath(configuration.getHealthCheckPath())
            .endHttpGet()
            .withTimeoutSeconds(healthCheckProperties.getReadinessProbeTimeoutSeconds())
            .build();
    }

    private Probe getLivenessProbe(RuntimeConfigSpec configuration) {
        HealthCheckProperties healthCheckProperties = configuration.getHealthCheckProperties();
        NetworkConfigSpec networkConfigSpec = configuration.getNetworkConfigSpec();
        return new ProbeBuilder()
            .withFailureThreshold(healthCheckProperties.getLivenessProbeFailureThreshold())
            .withInitialDelaySeconds(healthCheckProperties.getLivenessProbeInitialDelaySeconds())
            .withPeriodSeconds(healthCheckProperties.getLivenessProbePeriodSeconds())
            .withSuccessThreshold(healthCheckProperties.getLivenessProbeSuccessThreshold())
            .withNewTcpSocket()
            .withNewPort(networkConfigSpec.getInnerPort())
            .endTcpSocket()
            .withTimeoutSeconds(healthCheckProperties.getLivenessProbeTimeoutSeconds())
            .build();
    }
}
