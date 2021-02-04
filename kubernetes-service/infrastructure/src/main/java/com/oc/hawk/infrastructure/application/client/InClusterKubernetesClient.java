package com.oc.hawk.infrastructure.application.client;

import io.fabric8.kubernetes.client.AutoAdaptableKubernetesClient;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;

/**
 * @author kangta123
 */
public class InClusterKubernetesClient implements IKubernetesClient {
    @Override
    public KubernetesClient getClient() {
        Config config = new ConfigBuilder()
            .withTrustCerts(true)
            .withDisableHostnameVerification(true)
            .build();

        return new AutoAdaptableKubernetesClient(config);
    }
}
