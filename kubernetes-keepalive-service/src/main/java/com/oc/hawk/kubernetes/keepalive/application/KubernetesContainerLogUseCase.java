package com.oc.hawk.kubernetes.keepalive.application;

import com.oc.hawk.infrastructure.application.KubernetesApi;
import com.oc.hawk.infrastructure.application.KubernetesLog;
import com.oc.hawk.kubernetes.api.dto.KubernetesLogDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class KubernetesContainerLogUseCase {
    private final KubernetesApi kubernetesApi;

    public KubernetesLogDTO readFullLogs(String serviceName, String namespace, int index, Integer timestamp) {
        KubernetesLog kubernetesLog = kubernetesApi.readFullLogs(serviceName, namespace, index, timestamp);
        KubernetesLogDTO result = new KubernetesLogDTO();
        result.setData(kubernetesLog.getData());
        result.setTimestamp(kubernetesLog.getTimestamp());
        return result;
    }
}
