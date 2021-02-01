package com.oc.hawk.container.domain.model.runtime.config;

import com.oc.hawk.container.domain.config.ContainerConfiguration;

import java.util.Objects;

public class InstanceDomain {

    private final String serviceName;
    private final String namespace;

    public InstanceDomain(String serviceName, String namespace) {
        this.serviceName = serviceName;
        this.namespace = namespace;
    }

    public String getDomain(ContainerConfiguration containerConfiguration) {
        String domain = containerConfiguration.getDomainHost();
        if (Objects.equals(namespace, containerConfiguration.getDefaultInstanceNamespace())) {
            return serviceName + "." + domain;
        } else {
            return serviceName + "." + namespace + "." + domain;
        }
    }
}
