package com.oc.hawk.container.domain.model.runtime.config.volume;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 使用主机存储的volume
 * @author kangta123
 */

@Getter
public class HostedInstanceVolume extends InstanceVolume {

    public HostedInstanceVolume(String volumeName, String mountPath) {
        super(volumeName, mountPath);
    }
}
