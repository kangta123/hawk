package com.oc.hawk.container.domain.model.build.protocal;

import com.oc.hawk.container.domain.model.runtime.config.InstanceId;
import lombok.Getter;

@Getter
public class ProjectBuildDeployTo {
    private final InstanceId instanceId;

    private final Long buildJobId;

    public ProjectBuildDeployTo(InstanceId instanceId, Long buildJobId) {
        this.instanceId = instanceId;
        this.buildJobId = buildJobId;
    }
}
