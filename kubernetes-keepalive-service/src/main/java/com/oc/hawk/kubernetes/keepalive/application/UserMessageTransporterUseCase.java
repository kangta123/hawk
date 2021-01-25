package com.oc.hawk.kubernetes.keepalive.application;

import com.oc.hawk.kubernetes.keepalive.application.facade.ProjectFacade;
import com.oc.hawk.kubernetes.keepalive.application.facade.Transporter;
import com.oc.hawk.kubernetes.keepalive.port.driven.socket.WebSocketMessage;
import com.oc.hawk.kubernetes.keepalive.port.driven.socket.WebSocketTopic;
import com.oc.hawk.project.api.dto.ProjectBuildJobDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class UserMessageTransporterUseCase {
    private final Transporter transporter;
    private final ProjectFacade projectFacade;

    public void broadcastBuildLogToUser(long domainId, WebSocketMessage message) {
        String topic = getBroadcastBuildLogTopic(domainId);
        log.debug("Broadcast message to topic {} for {}", topic, message);
        transporter.send(topic, message);
    }

    private String getBroadcastBuildLogTopic(long domainId) {
        return WebSocketTopic.WS_BUILD_TOPIC.getTopic(String.valueOf(domainId));
    }

    @Async
    public void broadcastBuildJobToUser(long domainId) {
        ProjectBuildJobDTO projectBuildJob = projectFacade.getProjectBuildJob(domainId);
        this.broadcastBuildLogToUser(domainId, WebSocketMessage.buildJob(projectBuildJob));
    }
}
