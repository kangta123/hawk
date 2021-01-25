package com.oc.hawk.monitor.port.driven.facade;

import com.oc.hawk.kubernetes.api.constants.RuntimeInfoDTO;
import com.oc.hawk.monitor.port.driven.facade.feign.KubernetesGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author kangta123
 */
@Component
@RequiredArgsConstructor
public class RemoteKubernetesFacade implements KubernetesFacade {
    private final KubernetesGateway kubernetesGateway;

    @Override
    public String getAvailablePodName(String namespace, String app, String version, String name) {
        final List<RuntimeInfoDTO> availableRuntimeInfo = kubernetesGateway.getAvailableRuntimeInfo(namespace, app, version, name, true);
        if (availableRuntimeInfo.size() > 1) {
            return availableRuntimeInfo.stream().max((a, b) -> Math.round(a.getStartTime() - b.getStartTime()))
                .map(RuntimeInfoDTO::getPodName)
                .orElse(null);
        }

        if (availableRuntimeInfo.size() == 1) {
            return availableRuntimeInfo.iterator().next().getPodName();
        }

        return "";
    }
}
