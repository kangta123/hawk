package com.oc.hawk.kubernetes.runtime.application.runtime;

import com.google.common.collect.ImmutableMap;
import com.oc.hawk.infrastructure.application.KubernetesApi;
import com.oc.hawk.infrastructure.config.KubernetesAccessConfiguration;
import com.oc.hawk.kubernetes.domain.model.IServiceLabelConstants;
import com.oc.hawk.kubernetes.runtime.application.runtime.spec.RuntimeConfigSpec;
import com.oc.hawk.kubernetes.runtime.application.runtime.spec.SecretConfigSpec;
import com.oc.hawk.kubernetes.runtime.application.runtime.spec.deployment.RuntimeComponent;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodBuilder;
import io.fabric8.kubernetes.api.model.PodSpec;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KubernetesRuntimeSpecUseCase {
    private final String LOCAL_PROFILE = "local";
    private final KubernetesApi kubernetesApi;
    private final KubernetesAccessConfiguration accessConfiguration;
    @Value("${spring.profiles.active}")
    private String profile;

    public void createPod(RuntimeComponent runtimeComponent) {

        RuntimeConfigSpec configuration = runtimeComponent.getConfiguration();
        final String secret = new SecretConfigSpec(accessConfiguration, kubernetesApi).createDockerRegSecret(configuration.getNamespace());

        PodSpec podSpec = runtimeComponent.createPod().create();
        Pod pod = new PodBuilder()
            .withNewMetadata()
            .withName(runtimeComponent.getName())
            .addToLabels(runtimeComponent.getLabels())
            .endMetadata()
            .withNewSpecLike(podSpec)
            .addNewImagePullSecret(secret)
            .endSpec()
            .build();
        kubernetesApi.createPod(configuration.getNamespace(), pod);
    }

    public void createDeployment(RuntimeComponent runtimeComponent) {

        RuntimeConfigSpec configuration = runtimeComponent.getConfiguration();

        String name = runtimeComponent.getName();

        PodSpec podSpec = runtimeComponent.createPod().create();

        final String secret = new SecretConfigSpec(accessConfiguration, kubernetesApi).createDockerRegSecret(configuration.getNamespace());

        Deployment deployment = new DeploymentBuilder()
            .withNewMetadata()
            .withName(name)
            .endMetadata()
            .withNewSpec()
            .withReplicas(1)
            .withNewTemplate()
            .withNewMetadata()
            .addToLabels(runtimeComponent.getLabels())
            .addToAnnotations(runtimeComponent.getAnnotations())
            .endMetadata()
            .editOrNewSpecLike(podSpec)
            .addNewImagePullSecret(secret)
            .endSpec()
            .endTemplate()
            .withNewSelector()
            .addToMatchLabels(IServiceLabelConstants.LABEL_VERSION, name)
            .endSelector()
            .endSpec()
            .build();

        kubernetesApi.createDeployment(configuration.getNamespace(), deployment);
    }


}
