package com.oc.hawk.container.domain.model.runtime.config.volume;

public class AppInstanceVolume extends InstanceVolume {
    public AppInstanceVolume(String volumeName, String mountPath) {
        super(volumeName, mountPath);
    }
}
