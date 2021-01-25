package com.oc.hawk.container.domain.model.project;

import lombok.Getter;

@Getter
public class ProjectRuntimeConfig {
    private final ProjectRuntimeVolumeConfig volume;
    private final ProjectRuntimeImageConfig image;

    public ProjectRuntimeConfig(String buildImage, String dataImage, String appImage, ProjectRuntimeVolumeConfig projectRuntimeVolumeConfig) {
        this.image = new ProjectRuntimeImageConfig(appImage, buildImage, dataImage);
        this.volume = projectRuntimeVolumeConfig;
    }
}
