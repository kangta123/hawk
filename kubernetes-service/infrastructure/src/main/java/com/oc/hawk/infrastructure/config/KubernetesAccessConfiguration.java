package com.oc.hawk.infrastructure.config;

import lombok.Data;


@Data
public class KubernetesAccessConfiguration {
    private String apiServer;
    private String token;
    private String dockerConfigJson;
}
