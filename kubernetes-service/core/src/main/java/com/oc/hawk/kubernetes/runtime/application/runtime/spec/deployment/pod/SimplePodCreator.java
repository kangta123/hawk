package com.oc.hawk.kubernetes.runtime.application.runtime.spec.deployment.pod;

import com.oc.hawk.kubernetes.runtime.application.runtime.ServiceVolumeComponent;
import com.oc.hawk.kubernetes.runtime.application.runtime.spec.deployment.RuntimeComponent;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.PodSpec;

public class SimplePodCreator extends PodCreator {
    public SimplePodCreator(RuntimeComponent runtimeComponent) {
        super(runtimeComponent);
    }

    @Override
    public PodSpec create() {
        PodSpec podSpec = new PodSpec();

        Container appContainer = createAppContainer();
        podSpec.getContainers().add(appContainer);

        ServiceVolumeComponent volumeHolder = runtimeComponent.getServiceVolumeHolder();
        if (volumeHolder != null) {
            podSpec.setVolumes(volumeHolder.getVolumes());
        }

        return podSpec;
    }
}
