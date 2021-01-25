package com.oc.hawk.kubernetes.keepalive.application;

public interface KubernetesEventRepository {
    boolean isReceived(String resourceVersion);

    String loadEventResourceVersion();
    void updateEventResourceVersion(String version);

    void dropEventResourceVersion();
}
