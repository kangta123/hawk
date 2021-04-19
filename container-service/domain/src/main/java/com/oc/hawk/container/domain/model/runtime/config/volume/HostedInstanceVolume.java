package com.oc.hawk.container.domain.model.runtime.config.volume;

import lombok.Getter;

/**
 * 使用主机存储的volume
 * @author kangta123
 */

@Getter
public class HostedInstanceVolume extends InstanceVolume {

    public HostedInstanceVolume(String volumeName, String mountPath, String type) {
        super(volumeName, mountPath, type);
    }
}
