package com.oc.hawk.kubernetes.keepalive.application;

import com.oc.hawk.container.api.event.EntryPointUpdatedEvent;
import com.oc.hawk.kubernetes.api.constants.RuntimeInfoDTO;
import com.oc.hawk.kubernetes.api.dto.RuntimeStartFailedDto;
import com.oc.hawk.kubernetes.keepalive.application.facade.Transporter;
import com.oc.hawk.kubernetes.keepalive.port.driven.socket.WebSocketMessage;
import com.oc.hawk.kubernetes.keepalive.port.driven.socket.WebSocketTopic;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KubernetesRuntimeUseCase {
    private final Transporter transporter;

    public void runtimeStartFailed(RuntimeStartFailedDto data) {
        transporter.send(getWebSocketTopic(String.valueOf(data.getProjectId())), WebSocketMessage.runtimeFailed(data));
    }

    public void updateState(RuntimeInfoDTO runtimeInfoDTO) {
        String projectId = runtimeInfoDTO.getProjectId();
        String topic = getWebSocketTopic(projectId);
        transporter.send(topic, WebSocketMessage.runtime(runtimeInfoDTO));
    }

    public void updateEntryPoint(EntryPointUpdatedEvent serviceEntryPoint) {
        transporter.send(getWebSocketTopic(String.valueOf(serviceEntryPoint.getProjectId())), WebSocketMessage.entryPoint(serviceEntryPoint));
    }

    private String getWebSocketTopic(String projectId) {
        return WebSocketTopic.WS_PROJECT_SERVICE_EVENT.getTopic(projectId);
    }
}
