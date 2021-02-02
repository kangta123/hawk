package com.oc.hawk.container.runtime.port.driven.facade;

import com.oc.hawk.container.domain.model.runtime.config.InstanceName;
import com.oc.hawk.container.runtime.application.representation.RuntimeInfoFacade;
import com.oc.hawk.container.runtime.port.driven.facade.feign.KubernetesGateway;
import com.oc.hawk.kubernetes.api.constants.RuntimeInfoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RemoteRuntimeInfoFacade implements RuntimeInfoFacade {
    private final KubernetesGateway kubernetesGateway;

    @Override
    public List<RuntimeInfoDTO> queryRuntimeInfo(String namespace, Long projectId) {
        return kubernetesGateway.queryRuntimeInfo(namespace, projectId, null, false);
    }

    @Override
    public List<RuntimeInfoDTO> queryRuntimeInfo(String namespace, InstanceName instanceName) {
        return kubernetesGateway.queryRuntimeInfo(namespace, null, instanceName.getName(), true);
    }
}
