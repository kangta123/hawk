package com.oc.hawk.infrastructure.application;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PodExecution {
    private String name;
    private String namespace;
    private String command;
    private long domainId;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PodExecution{");
        sb.append("name='").append(name).append('\'');
        sb.append(", namespace='").append(namespace).append('\'');
        sb.append(", command='").append(command).append('\'');
        sb.append(", domainId='").append(domainId).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
