package com.oc.hawk.kubernetes;

import com.oc.hawk.common.spring.config.AsyncConfiguration;
import com.oc.hawk.common.spring.config.BaseConfiguration;
import com.oc.hawk.common.spring.config.WebConfiguration;
import com.oc.hawk.common.spring.config.WebSocketBrokerConfiguration;
import com.oc.hawk.infrastructure.config.KubernetesAccessConfiguration;
import com.oc.hawk.infrastructure.port.driven.KubernetesClientFactory;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(value = {WebConfiguration.class, BaseConfiguration.class, WebSocketBrokerConfiguration.class, AsyncConfiguration.class})
@Slf4j
@EnableConfigurationProperties
public class KubernetesKeepAliveApplication {
    public static void main(String[] args) {
        SpringApplication.run(KubernetesKeepAliveApplication.class, args);
    }

    @Bean
    public KubernetesClient kubernetesClient() {
        return KubernetesClientFactory.getClient(kubernetesConfiguration());
    }

    @Bean
    @ConfigurationProperties(prefix = "hawk.kubernetes")
    public KubernetesAccessConfiguration kubernetesConfiguration() {
        return new KubernetesAccessConfiguration();
    }
}

