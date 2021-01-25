package com.oc.hawk.infrastructure.application.representation;

import com.oc.hawk.common.utils.DateUtils;
import com.oc.hawk.infrastructure.application.KubernetesPod;
import com.oc.hawk.kubernetes.api.constants.RuntimeInfoDTO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class KubernetesPodRepresentation {

    public RuntimeInfoDTO toRuntimeInfoDTO(KubernetesPod kubernetesRuntime) {
        if (kubernetesRuntime == null) {
            return null;
        }
        RuntimeInfoDTO runtimeInfoDTO = new RuntimeInfoDTO();

        String runtimeStatus = kubernetesRuntime.getRuntimeStatus();
        runtimeInfoDTO.setStatus(runtimeStatus);

        runtimeInfoDTO.setRestartCount(kubernetesRuntime.getRestartCount());

        String runtimeImage = kubernetesRuntime.getRuntimeImage();
        runtimeInfoDTO.setImage(runtimeImage);
        runtimeInfoDTO.setTag(StringUtils.substringAfterLast(runtimeImage, ":"));
        runtimeInfoDTO.setVersion(kubernetesRuntime.getResourceVersion());

        runtimeInfoDTO.setName(kubernetesRuntime.getName());
        runtimeInfoDTO.setPodName(kubernetesRuntime.getRuntimeId());
        runtimeInfoDTO.setServiceName(kubernetesRuntime.getServiceName());

        runtimeInfoDTO.setNamespace(kubernetesRuntime.getNamespace());

        runtimeInfoDTO.setNodeIp(kubernetesRuntime.getNodeIp());

        runtimeInfoDTO.setProjectId(kubernetesRuntime.getProjectId());
        runtimeInfoDTO.setId(kubernetesRuntime.getRuntimeId());
        LocalDateTime startTime = kubernetesRuntime.getStartTime();
        if (startTime != null) {
            runtimeInfoDTO.setStartTime(DateUtils.toLong(startTime));
        }
        return runtimeInfoDTO;
    }
}
