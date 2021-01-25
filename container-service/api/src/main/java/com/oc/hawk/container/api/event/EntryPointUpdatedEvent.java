package com.oc.hawk.container.api.event;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor
public class EntryPointUpdatedEvent {
    private String namespace;
    private String serviceName;
    private Map<Integer, Integer> exposePorts;
    private long projectId;

    public EntryPointUpdatedEvent(String namespace, String serviceName, Map<Integer, Integer> exposePorts, long projectId) {
        this.namespace = namespace;
        this.serviceName = serviceName;
        this.exposePorts = exposePorts;
        this.projectId = projectId;
    }
}
