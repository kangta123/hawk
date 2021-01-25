package com.oc.hawk.container.domain.model.project;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProjectRuntimeVolumeConfig {
    private String volume;
    private String mountPath;

    public ProjectRuntimeVolumeConfig(String volume, String mountPath) {
        this.volume = volume;
        this.mountPath = mountPath;
    }
}
