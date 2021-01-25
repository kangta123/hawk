package com.oc.hawk.kubernetes.runtime.application.runtime.spec.container;

import com.google.common.collect.Lists;
import com.oc.hawk.kubernetes.runtime.application.runtime.ServiceVolumeComponent;
import com.oc.hawk.kubernetes.runtime.application.runtime.spec.RuntimeConfigSpec;
import com.oc.hawk.infrastructure.application.KubernetesConstants;
import com.oc.hawk.kubernetes.domain.model.IServiceLabelConstants;
import com.oc.hawk.kubernetes.runtime.application.runtime.spec.deployment.RuntimeComponent;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.EnvVar;
import io.fabric8.kubernetes.api.model.EnvVarBuilder;
import io.fabric8.kubernetes.api.model.SecurityContext;

public class InitContainerSpec implements ContainerSpec {
    static final String START_COMMAND = "sh app.sh";

    @Override
    public Container getContainer(RuntimeComponent runtimeComponent) {
        RuntimeConfigSpec configuration = runtimeComponent.getConfiguration();

        Container initContainer = new Container();
        SecurityContext securityContext = new SecurityContext();
        securityContext.setPrivileged(true);
        initContainer.setSecurityContext(securityContext);
        initContainer.setImage(configuration.getImageName() );

        EnvVar envVar = new EnvVarBuilder().withName(IServiceLabelConstants.LABEL_SERVICE_NAME).withValue(configuration.getName()).build();
        initContainer.getEnv().add(envVar);

        initContainer.setName(KubernetesConstants.INIT_CONTAINER_NAME);
        initContainer.setCommand(Lists.newArrayList(START_COMMAND.split(" ")));


        ServiceVolumeComponent volumeHolder = runtimeComponent.getServiceVolumeHolder();
        initContainer.setVolumeMounts(volumeHolder.getVolumeMounts(0));
        return initContainer;
    }


}
