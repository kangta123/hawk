package com.oc.hawk.kubernetes.runtime.application.runtime;

import com.google.common.collect.Lists;
import com.oc.hawk.infrastructure.application.KubernetesApi;
import com.oc.hawk.infrastructure.application.KubernetesPod;
import com.oc.hawk.infrastructure.application.representation.KubernetesPodRepresentation;
import com.oc.hawk.kubernetes.api.constants.RuntimeInfoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class KubernetesRuntimeUseCase {
    private final KubernetesApi kubernetesApi;

    private final KubernetesPodRepresentation kubernetesPodRepresentation;


    public List<RuntimeInfoDTO> queryRuntimeInfo(String namespace, Long projectId, boolean podReady, String name, String version, String app) {
        List<KubernetesPod> kubernetesPodList = Lists.newArrayList();
        if (StringUtils.isNotEmpty(name)) {
            kubernetesPodList.add(kubernetesApi.getPod(namespace, name));
        } else {
            kubernetesPodList = kubernetesApi.queryPod(namespace, projectId, app, version);
        }
        return kubernetesPodList.stream().filter(Objects::nonNull).sorted().filter(p -> {
            if (podReady) {
                return p.isReady();
            }
            return true;
        }).filter(p -> !p.isDeleting()).map(kubernetesPodRepresentation::toRuntimeInfoDTO).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public void scaleService(String namespace, String name, int value) {
        log.info("scale runtime {} count to {}", name, value);
        kubernetesApi.getDeployment(namespace, name).scale(value);
    }


}
