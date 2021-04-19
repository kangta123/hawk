package com.oc.hawk.container.domain.model.runtime.build;

import com.oc.hawk.container.domain.model.runtime.config.volume.HostedInstanceVolume;
import com.oc.hawk.container.domain.model.runtime.config.volume.InstanceVolume;
import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author kangta123
 */
@AllArgsConstructor
public enum ProjectBuildVolume {
    dockerExec("docker-volume", "/usr/bin/docker:/usr/bin/docker", null),
    dockerSock("docker-sock-volume", "/var/run/docker.sock:/var/run/docker.sock", "Socket");
    private final String name;
    private final String path;
    private final String type;


    public static List<InstanceVolume> defaultVolumes() {
        return Arrays.stream(ProjectBuildVolume.values()).map(v -> new HostedInstanceVolume(v.name, v.path, v.type)).collect(Collectors.toList());
    }
}
