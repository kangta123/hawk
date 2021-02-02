package com.oc.hawk.container.domain.model.runtime.config.volume;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author kangta123
 */

@Getter
public class BuildInstanceVolume extends InstanceVolume {
    private final boolean host;

    private BuildInstanceVolume(String volumeName, String mountPath) {
        super(volumeName, mountPath);
        host = true;
    }

    public static List<InstanceVolume> defaultDockerVolumes() {
        return Arrays.stream(DockerVolume.values()).map(v -> new BuildInstanceVolume(v.name, v.path)).collect(Collectors.toList());
    }


    enum DockerVolume {
        dockerExec("docker-volume", "/usr/bin/docker:/usr/bin/docker"),
        dockerSock("docker-sock-volume", "/var/run/docker.sock:/var/run/docker.sock");
        private final String name;
        private final String path;

        DockerVolume(String name, String path) {
            this.name = name;
            this.path = path;
        }
    }

}
