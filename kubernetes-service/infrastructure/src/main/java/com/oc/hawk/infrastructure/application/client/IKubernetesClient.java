package com.oc.hawk.infrastructure.application.client;

public interface IKubernetesClient {
    public io.fabric8.kubernetes.client.KubernetesClient getClient();
}

