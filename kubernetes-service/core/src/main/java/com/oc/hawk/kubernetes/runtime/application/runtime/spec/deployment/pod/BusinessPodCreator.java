package com.oc.hawk.kubernetes.runtime.application.runtime.spec.deployment.pod;

import com.google.common.collect.Lists;
import com.oc.hawk.kubernetes.runtime.application.runtime.spec.container.InitContainerSpec;
import com.oc.hawk.kubernetes.runtime.application.runtime.spec.deployment.RuntimeComponent;
import io.fabric8.kubernetes.api.model.PodSpec;

public class BusinessPodCreator extends PodCreator {

    public BusinessPodCreator(RuntimeComponent runtimeComponent) {
        super(runtimeComponent);
    }

    @Override
    public PodSpec create() {

        PodSpec podSpec = new PodSpec();

        podSpec.setInitContainers(Lists.newArrayList(new InitContainerSpec().getContainer(runtimeComponent)));
        podSpec.setContainers(Lists.newArrayList(createAppContainer()));

        podSpec.setVolumes(runtimeComponent.getServiceVolumeHolder().getVolumes());
        return podSpec;
    }


}
