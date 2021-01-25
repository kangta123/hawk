package com.oc.hawk.container.api.command;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeleteRuntimeInfoCommand {
    private String namespace;
    private String name;
    private String serviceName;

    public DeleteRuntimeInfoCommand(String namespace, String name, String serviceName) {
        this.namespace = namespace;
        this.name = name;
        this.serviceName = serviceName;
    }
}
