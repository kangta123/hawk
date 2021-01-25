package com.oc.hawk.kubernetes.runtime.application.runtime.spec.deployment.decorator;

import com.oc.hawk.kubernetes.runtime.application.runtime.spec.RuntimeConfigSpec;
import com.oc.hawk.kubernetes.runtime.application.runtime.spec.NetworkConfigSpec;
import com.oc.hawk.kubernetes.runtime.application.runtime.spec.deployment.ConfigurableRuntimeComponent;
import com.oc.hawk.kubernetes.runtime.application.runtime.spec.deployment.RuntimeComponent;

import java.util.Objects;

public class ServiceMeshDecorator extends AbstractConfigurableRuntimeDecorator {
    private final String SERVICE_MESH_DISABLE_KEY = "sidecar.istio.io/inject";

    public ServiceMeshDecorator( ConfigurableRuntimeComponent component) {
        super( component);
    }

    @Override
    public void config(RuntimeComponent runtimeComponent) {
        super.config(runtimeComponent);
        RuntimeConfigSpec configuration = runtimeComponent.getConfiguration();
        NetworkConfigSpec networkConfigSpec = configuration.getNetworkConfigSpec();
        if (Objects.equals(networkConfigSpec.getMesh(), Boolean.FALSE)) {
            runtimeComponent.addAnnotation(SERVICE_MESH_DISABLE_KEY, Boolean.FALSE.toString());
        }
    }
}
