package com.oc.hawk.container.domain.model.runtime.build;

import com.oc.hawk.container.domain.config.ContainerRuntimeConfig;
import com.oc.hawk.container.domain.model.runtime.config.volume.HostedInstanceVolume;
import com.oc.hawk.container.domain.model.runtime.config.volume.InstanceVolume;
import com.oc.hawk.container.domain.model.runtime.config.volume.NormalInstanceVolume;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author kangta123
 */
public enum ProjectBuildVolume {
    dockerExec("docker-volume", "/usr/bin/docker:/usr/bin/docker"),
    dockerSock("docker-sock-volume", "/var/run/docker.sock:/var/run/docker.sock");
    private final String name;
    private final String path;

    ProjectBuildVolume(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public static List<InstanceVolume> defaultVolumes() {
        return Arrays.stream(ProjectBuildVolume.values()).map(v -> new HostedInstanceVolume(v.name, v.path)).collect(Collectors.toList());
    }
}
