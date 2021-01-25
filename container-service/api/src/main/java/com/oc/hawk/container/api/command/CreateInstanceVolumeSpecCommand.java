package com.oc.hawk.container.api.command;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateInstanceVolumeSpecCommand {
    private String mountPath;
    private String volumeName;

    private boolean host;

    private String subPath;

    public CreateInstanceVolumeSpecCommand(String mountPath, String volumeName, boolean host, String subPath) {
        this.mountPath = mountPath;
        this.volumeName = volumeName;
        this.host = host;
        this.subPath = subPath;
    }

    public CreateInstanceVolumeSpecCommand(String mountPath, String volumeName, boolean host) {
        this(mountPath, volumeName);
        this.host = host;
    }

    public CreateInstanceVolumeSpecCommand(String mountPath, String volumeName) {
        this.mountPath = mountPath;
        this.volumeName = volumeName;
        this.host = false;
    }
}
