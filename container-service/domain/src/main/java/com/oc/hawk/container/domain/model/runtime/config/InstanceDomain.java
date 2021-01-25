package com.oc.hawk.container.domain.model.runtime.config;

import com.oc.hawk.container.domain.config.ContainerConfiguration;

import java.util.Objects;

public class InstanceDomain {

    public static final String DEFAULT_NAMESPACE = "default";
    private String serviceName;
    private String namespace;

    public InstanceDomain(String serviceName, String namespace) {
        this.serviceName = serviceName;
        this.namespace = namespace;
    }

    public String getDomain(ContainerConfiguration containerConfiguration) {
        String domain = containerConfiguration.getDomainHost();
        if (Objects.equals(namespace, DEFAULT_NAMESPACE)) {
            return serviceName + "." + domain;
        } else {
            return serviceName + "." + namespace + "." + domain;
        }
    }
}
