package com.oc.hawk.container.api.command;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StopRuntimeInfoCommand {
    private Long id;
    private String namespace;
    private String name;
    private long projectId;
    private String message;

    public StopRuntimeInfoCommand(Long id, String namespace, String name, long projectId, String message) {
        this.id = id;
        this.namespace = namespace;
        this.name = name;
        this.projectId = projectId;
        this.message = message;
    }

    public StopRuntimeInfoCommand(Long id, String namespace, String name, long projectId) {
        this.id = id;
        this.namespace = namespace;
        this.name = name;
        this.projectId = projectId;
    }
}
