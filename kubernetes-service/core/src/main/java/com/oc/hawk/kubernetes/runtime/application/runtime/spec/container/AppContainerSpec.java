package com.oc.hawk.kubernetes.runtime.application.runtime.spec.container;

import com.oc.hawk.container.domain.model.runtime.info.PerformanceLevel;
import com.oc.hawk.infrastructure.application.KubernetesConstants;
import com.oc.hawk.kubernetes.runtime.application.runtime.spec.RuntimeConfigSpec;
import com.oc.hawk.kubernetes.runtime.application.runtime.spec.deployment.RuntimeComponent;
import com.oc.hawk.kubernetes.runtime.application.runtime.spec.deployment.component.DefaultPodResourceRequirement;
import com.oc.hawk.kubernetes.runtime.application.runtime.spec.deployment.component.PodResourceRequirement;
import io.fabric8.kubernetes.api.model.*;

import java.util.List;

public class AppContainerSpec implements ContainerSpec {
    private final String ENV_PERFORMANCE_LEVEL_KEY = "PERFORMANCE_LEVEL";
    private final PodResourceRequirement resourceRequirement;
    private final String POD_NAMESPACE = "POD_NAMESPACE";
    private final String NAMESPACE_FIELD_PATH = "metadata.namespace";

    public AppContainerSpec() {
        this.resourceRequirement = new DefaultPodResourceRequirement();
    }

    @Override
    public Container getContainer(RuntimeComponent runtimeComponent) {
        RuntimeConfigSpec configuration = runtimeComponent.getConfiguration();

        Container appContainer = new Container();
        appContainer.setName(KubernetesConstants.APP_CONTAINER_NAME);

        appContainer.setImage(configuration.getAppImage().getImage());

        appContainer.setTty(true);
        appContainer.setStdin(true);
        ResourceRequirements resourceRequirement = resourceRequirement(runtimeComponent, configuration);

        if (resourceRequirement != null) {
            appContainer.setResources(resourceRequirement);
        }
        if (configuration.isBusiness()) {
            new AppContainerProbe(appContainer).configContainerProbe(configuration);
        }

        final List<EnvVar> envVars = runtimeComponent.getEnvVars();
        envVars.add(injectNamespaceToEnv());
        appContainer.setEnv(envVars);

        appContainer.setVolumeMounts(runtimeComponent.getServiceVolumeHolder().getVolumeMounts(1));
        return appContainer;
    }

    private EnvVar injectNamespaceToEnv() {
        final EnvVarSource envVarSource = new EnvVarSourceBuilder()
            .withFieldRef(
                new ObjectFieldSelectorBuilder().withFieldPath(NAMESPACE_FIELD_PATH).build())
            .build();
        return new EnvVar(POD_NAMESPACE, null, envVarSource);
    }

    private ResourceRequirements resourceRequirement(RuntimeComponent runtimeComponent, RuntimeConfigSpec configuration) {
        PerformanceLevel performanceLevel = configuration.getPerformanceLevelOrDefault();
        ResourceRequirements resourceRequirement = this.resourceRequirement.getResourceRequirement(performanceLevel);

        runtimeComponent.addEnv(ENV_PERFORMANCE_LEVEL_KEY, performanceLevel.name());
        return resourceRequirement;
    }
}
