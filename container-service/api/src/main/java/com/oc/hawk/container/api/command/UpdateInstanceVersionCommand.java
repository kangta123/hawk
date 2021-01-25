package com.oc.hawk.container.api.command;

import lombok.Getter;

@Getter
public class UpdateInstanceVersionCommand {
    public UpdateInstanceVersionCommand(Long instanceId, String version) {
        this.instanceId = instanceId;
        this.version = version;
    }

    private Long instanceId;
    private String version;
}
