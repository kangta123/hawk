package com.oc.hawk.kubernetes.runtime.application.runtime.spec.container;

import com.oc.hawk.kubernetes.runtime.application.runtime.spec.deployment.RuntimeComponent;
import io.fabric8.kubernetes.api.model.Container;

public interface ContainerSpec {
    Container getContainer(RuntimeComponent configuration);
}
