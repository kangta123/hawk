package com.oc.hawk.infrastructure.application;

public interface KubernetesConstants {
    String DEFAULT_NAMESPACE = "default";
    String DEFAULT_APP_NAMESPACE = "app";
    String DOCKER_ID_PREFIX = "docker://";
    String APP_CONTAINER_NAME = "app";
    String INIT_CONTAINER_NAME = "app-data";


}
