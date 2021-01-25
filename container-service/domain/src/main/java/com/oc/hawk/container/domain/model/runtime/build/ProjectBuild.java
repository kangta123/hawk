package com.oc.hawk.container.domain.model.runtime.build;

import com.oc.hawk.container.domain.model.runtime.config.InstanceImageVersion;
import lombok.Getter;

@Getter
public class ProjectBuild {
    private ProjectBuildPost post;
    private InstanceImageVersion version;

    public ProjectBuild(ProjectBuildPost post, InstanceImageVersion version) {
        this.post = post;
        this.version = version;
    }
}
