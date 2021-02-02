package com.oc.hawk.kubernetes.runtime.application.runtime.spec.deployment.pod;

import com.oc.hawk.kubernetes.runtime.application.runtime.spec.container.AppContainerSpec;
import com.oc.hawk.kubernetes.runtime.application.runtime.spec.deployment.RuntimeComponent;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.PodSpec;

public abstract class PodCreator {
    protected RuntimeComponent runtimeComponent;

    public PodCreator(RuntimeComponent runtimeComponent) {
        this.runtimeComponent = runtimeComponent;
    }

    public abstract PodSpec create();

    protected Container createAppContainer() {
        return new AppContainerSpec().getContainer(runtimeComponent);
    }
}
