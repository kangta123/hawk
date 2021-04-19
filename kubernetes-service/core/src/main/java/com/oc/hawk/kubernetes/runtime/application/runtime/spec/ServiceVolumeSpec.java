package com.oc.hawk.kubernetes.runtime.application.runtime.spec;

import com.oc.hawk.kubernetes.runtime.application.runtime.spec.container.VolumeFileType;
import com.oc.hawk.kubernetes.runtime.application.runtime.spec.container.VolumeType;
import lombok.Getter;

@Getter
public class ServiceVolumeSpec {
    private final String mountPath;
    private final String volumeName;

    private final VolumeType type;
    private final String subPath;
    private final VolumeFileType volumeFileType;

    public ServiceVolumeSpec(String mountPath, String volumeName, VolumeType type, String subPath, VolumeFileType volumeFileType) {
        this.mountPath = mountPath;
        this.volumeName = volumeName;
        this.type = type;
        this.subPath = subPath;
        this.volumeFileType = volumeFileType;
    }

    public ServiceVolumeSpec(String mountPath, String volumeName, VolumeType type) {
        this(mountPath, volumeName, type, null, null);
    }
}
