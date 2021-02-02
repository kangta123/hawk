package com.oc.hawk.container.domain.model.runtime.config.volume;

import lombok.Getter;

@Getter
public class LogInstanceVolume extends InstanceVolume {
    private final String subPath;

    public LogInstanceVolume(String volumeName, String mountPath, String subPath) {
        super(volumeName, mountPath);
        this.subPath = subPath;
    }
}
