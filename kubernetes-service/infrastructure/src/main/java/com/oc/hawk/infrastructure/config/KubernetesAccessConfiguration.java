package com.oc.hawk.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;


@Data
public class KubernetesAccessConfiguration {
    private String apiServer;
    private String token;
    private String dockerConfigJson;
}
