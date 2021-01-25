package com.oc.hawk.container.domain.model.runtime.build;

import com.oc.hawk.container.domain.model.runtime.config.InstanceId;
import lombok.Getter;

@Getter
public class ProjectBuildPost {
    private InstanceId instanceId;
    private String instanceName;

    public ProjectBuildPost(InstanceId instanceId, String instanceName) {
        this.instanceId = instanceId;
        this.instanceName = instanceName;
    }
}
