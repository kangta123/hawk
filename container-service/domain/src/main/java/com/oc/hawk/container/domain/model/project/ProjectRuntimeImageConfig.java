package com.oc.hawk.container.domain.model.project;

import lombok.Getter;

@Getter
public class ProjectRuntimeImageConfig {
    private final String appImage;
    private final String buildImage;
    private final String dataImage;

    public ProjectRuntimeImageConfig(String appImage, String buildImage, String dataImage) {
        this.appImage = appImage;
        this.buildImage = buildImage;
        this.dataImage = dataImage;
    }

}
