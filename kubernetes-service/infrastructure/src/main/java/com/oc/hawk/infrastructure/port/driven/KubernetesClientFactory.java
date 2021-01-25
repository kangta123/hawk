package com.oc.hawk.infrastructure.port.driven;

import com.oc.hawk.infrastructure.config.KubernetesAccessConfiguration;
import io.fabric8.kubernetes.client.AutoAdaptableKubernetesClient;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;

public class KubernetesClientFactory {
    public static KubernetesClient getClient(KubernetesAccessConfiguration kubernetesAccessConfiguration) {
        Config config = new ConfigBuilder()
                .withTrustCerts(true)
                .withOauthToken(kubernetesAccessConfiguration.getToken())
                .withDisableHostnameVerification(true)
                .withMasterUrl(kubernetesAccessConfiguration.getApiServer()).withDisableHostnameVerification(true)
                .build();

        return new AutoAdaptableKubernetesClient(config);
    }
}
