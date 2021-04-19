package com.oc.hawk.container.api.command;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateInstanceVolumeSpecCommand {
    private String mountPath;
    private String volumeName;

    private boolean host;
    private String type;

    private String subPath;

    public CreateInstanceVolumeSpecCommand(String mountPath, String volumeName, boolean host, String subPath, String type) {
        this.mountPath = mountPath;
        this.volumeName = volumeName;
        this.host = host;
        this.subPath = subPath;
        this.type = type;
    }

    public CreateInstanceVolumeSpecCommand(String mountPath, String volumeName, boolean host, String subPath) {
        this(mountPath, volumeName, host, subPath, null);
    }


    public CreateInstanceVolumeSpecCommand(String mountPath, String volumeName, boolean host) {
        this(mountPath, volumeName, host, null);
    }

}
