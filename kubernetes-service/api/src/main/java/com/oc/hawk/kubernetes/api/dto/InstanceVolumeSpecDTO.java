package com.oc.hawk.kubernetes.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InstanceVolumeSpecDTO {
    private String mountPath;
    private String volumeName;

    private Boolean host;

    public InstanceVolumeSpecDTO(String mountPath, String volumeName, boolean host) {
        this(mountPath, volumeName);
        this.host = host;
    }

    public InstanceVolumeSpecDTO(String mountPath, String volumeName) {
        this.mountPath = mountPath;
        this.volumeName = volumeName;
        this.host = false;
    }
}
