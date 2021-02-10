package com.oc.hawk.container.domain.model.runtime.config.volume;

import lombok.Getter;

@Getter
public class SharedInstanceVolume extends InstanceVolume {
    private final String subPath;

    public SharedInstanceVolume(String volumeName, String mountPath, String subPath) {
        super(volumeName, mountPath);
        this.subPath = subPath;
    }
}
