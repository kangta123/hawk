package com.oc.hawk.kubernetes.runtime.application.runtime.spec;

import com.google.common.collect.ImmutableMap;
import com.oc.hawk.infrastructure.application.KubernetesApi;
import com.oc.hawk.infrastructure.config.KubernetesAccessConfiguration;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@RequiredArgsConstructor
public class SecretConfigSpec {
    private final KubernetesAccessConfiguration accessConfiguration;
    private final String DOCKER_REG_SECRET_KEY = ".dockerconfigjson";
    private final KubernetesApi kubernetesApi;

    public String createDockerRegSecret(String namespace) {
        final String dockerConfigJson = accessConfiguration.getDockerConfigJson();
        final String dockerSecretName = accessConfiguration.getDockerSecretName();
        if (StringUtils.isEmpty(dockerConfigJson) || StringUtils.isEmpty(dockerSecretName)) {
            return null;
        }
        kubernetesApi.createSecretIfNotExist(namespace, dockerSecretName, ImmutableMap.of(DOCKER_REG_SECRET_KEY, dockerConfigJson));
        return dockerSecretName;
    }
}
