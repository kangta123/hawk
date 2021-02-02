package com.oc.hawk.kubernetes.runtime.application.runtime.spec;

import com.oc.hawk.kubernetes.runtime.application.runtime.spec.container.VolumeType;
import lombok.Getter;

@Getter
public class ServiceVolumeSpec {
    private final String mountPath;
    private final String volumeName;

    private final VolumeType type;
    private String subPath;

    public ServiceVolumeSpec(String mountPath, String volumeName, VolumeType type, String subPath) {
        this.mountPath = mountPath;
        this.volumeName = volumeName;
        this.type = type;
        this.subPath = subPath;
    }

    public ServiceVolumeSpec(String mountPath, String volumeName, VolumeType type) {
        this.mountPath = mountPath;
        this.volumeName = volumeName;
        this.type = type;
    }
}
