package com.oc.hawk.kubernetes.runtime.application.runtime.spec.deployment.decorator;

import com.oc.hawk.kubernetes.runtime.application.runtime.spec.ServiceVolumeSpec;
import com.oc.hawk.kubernetes.runtime.application.runtime.spec.container.VolumeType;
import com.oc.hawk.kubernetes.runtime.application.runtime.spec.deployment.ConfigurableRuntimeComponent;
import com.oc.hawk.kubernetes.runtime.application.runtime.spec.deployment.RuntimeComponent;

public class ServiceVolumeDecorator extends AbstractConfigurableRuntimeDecorator {


    public static final String FILES_VOLUME = "files-volume";
    public static final String PLATFORM_VOLUME = "platform-volume";

    public ServiceVolumeDecorator(ConfigurableRuntimeComponent component) {
        super(component);
    }

    @Override
    public void config(RuntimeComponent runtimeComponent) {
        super.config(runtimeComponent);

//        runtimeComponent.addVolume(new ServiceVolumeSpec("/app/shared", PLATFORM_VOLUME, VolumeType.pvc));
        runtimeComponent.addVolume(new ServiceVolumeSpec("/tmp/files:/app/files", FILES_VOLUME, VolumeType.empty));
    }
}
