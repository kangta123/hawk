package com.oc.hawk.container.api.command;

import lombok.Getter;

@Getter
public class UpdateInstanceVersionCommand {
    private final Long instanceId;
    private final String version;

    public UpdateInstanceVersionCommand(Long instanceId, String version) {
        this.instanceId = instanceId;
        this.version = version;
    }
}
