package com.oc.hawk.infrastructure.application.client;

import com.oc.hawk.infrastructure.config.KubernetesAccessConfiguration;
import io.fabric8.kubernetes.client.AutoAdaptableKubernetesClient;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KubernetesClientImpl implements IKubernetesClient {
    private final KubernetesAccessConfiguration kubernetesAccessConfiguration;

    @Override
    public KubernetesClient getClient() {
        Config config = new ConfigBuilder()
                .withTrustCerts(true)
                .withOauthToken(kubernetesAccessConfiguration.getToken())
                .withDisableHostnameVerification(true)
                .withMasterUrl(kubernetesAccessConfiguration.getApiServer()).withDisableHostnameVerification(true)
                .build();

        return new AutoAdaptableKubernetesClient(config);
    }
}
