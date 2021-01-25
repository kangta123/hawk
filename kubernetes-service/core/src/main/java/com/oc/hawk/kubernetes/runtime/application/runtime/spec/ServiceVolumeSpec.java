package com.oc.hawk.kubernetes.runtime.application.runtime.spec;

import com.oc.hawk.kubernetes.runtime.application.runtime.spec.container.VolumeType;
import lombok.Getter;

@Getter
public class ServiceVolumeSpec {
    private String mountPath;
    private String volumeName;

    private VolumeType type;

    public ServiceVolumeSpec(String mountPath, String volumeName, VolumeType type, String subPath) {
        this.mountPath = mountPath;
        this.volumeName = volumeName;
        this.type = type;
        this.subPath = subPath;
    }

    private String subPath;
    public ServiceVolumeSpec(String mountPath, String volumeName, VolumeType type) {
        this.mountPath = mountPath;
        this.volumeName = volumeName;
        this.type = type;
    }
}
