package com.oc.hawk.container.domain.model.project;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProjectBuildVolumeConfig {
    private String volume;
    private String mountPath;

    public ProjectBuildVolumeConfig(String volume, String mountPath) {
        this.volume = volume;
        this.mountPath = mountPath;
    }
}
