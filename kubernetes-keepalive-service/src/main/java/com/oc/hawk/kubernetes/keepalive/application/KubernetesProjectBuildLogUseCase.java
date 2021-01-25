package com.oc.hawk.kubernetes.keepalive.application;

import com.google.common.base.Stopwatch;
import com.oc.hawk.api.constant.KafkaTopic;
import com.oc.hawk.container.domain.model.build.protocal.ProjectBuildMessageHandler;
import com.oc.hawk.container.domain.model.build.protocal.RuntimeProjectBuildMessageHandler;
import com.oc.hawk.ddd.event.DomainEvent;
import com.oc.hawk.ddd.event.EventPublisher;
import com.oc.hawk.infrastructure.application.KubernetesApi;
import com.oc.hawk.infrastructure.application.exception.KubeExecutionException;
import com.oc.hawk.infrastructure.application.exception.PodNotReadyException;
import com.oc.hawk.infrastructure.application.exception.PodStartFailedException;
import com.oc.hawk.kubernetes.keepalive.port.driven.socket.WebSocketMessage;
import com.oc.hawk.project.api.command.AddProjectBuildStageCommand;
import com.oc.hawk.project.api.command.UpdateProjectAppCommand;
import com.oc.hawk.project.api.event.ProjectDomainEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@RequiredArgsConstructor
public class KubernetesProjectBuildLogUseCase {

    private static final String RUNTIME_READY_TEXT = "构建服务已启动, 耗时%ss\r\n";
    private static final String RUNTIME_STARTING_TEXT = "构建服务启动中...\r\n";
    private final EventPublisher domainEventPublisher;
    private final KubernetesApi kubernetesApi;
    private final UserMessageTransporterUseCase userMessageTransporterUseCase;


    @Async
    public void asyncWatchLog(String namespace, String name, long domainId) {
        try {
            doWatch(namespace, name, domainId);
        } catch (PodNotReadyException e) {
            try {
                doWatch(name, namespace, domainId);
            } catch (Exception exception) {
                log.error("Could not execute command for {}", exception.getMessage(), exception);
            }
        } catch (PodStartFailedException e) {
            log.error("Runtime start failed, {}", name);
        }
    }


    private void doWatch(String namespace, String name, long domainId) {
        userMessageTransporterUseCase.broadcastBuildLogToUser(domainId, WebSocketMessage.text(RUNTIME_STARTING_TEXT));

        log.info("Kubernetes  watch pod log  {}", name);
        Stopwatch stopwatch = Stopwatch.createStarted();

        handleBuildMessage(namespace, name, domainId);

        userMessageTransporterUseCase.broadcastBuildLogToUser(domainId, WebSocketMessage.text(String.format(RUNTIME_READY_TEXT, stopwatch.elapsed(TimeUnit.SECONDS))));

        log.info("Kubernetes pod already started take {}ms", stopwatch.elapsed(TimeUnit.MILLISECONDS));

    }

    private void handleBuildMessage(String namespace, String name, long domainId) {
        final ProjectBuildMessageHandler projectBuildMessageHandler = new RuntimeProjectBuildMessageHandler(protocolMessage -> {
            if (StringUtils.isNotEmpty(protocolMessage.getText())) {
                userMessageTransporterUseCase.broadcastBuildLogToUser(domainId, WebSocketMessage.text(protocolMessage.getText()));
            }
            protocolMessage.getTags().forEach(tag -> {
                AddProjectBuildStageCommand addProjectBuildStageCommand = new AddProjectBuildStageCommand(tag.getTag(), tag.isSuccess(), tag.getData());
                domainEventPublisher.publishEvent(KafkaTopic.DOMAIN_EVENT_TOPIC, DomainEvent.byData(domainId, ProjectDomainEventType.PROJECT_BUILD_UPDATE_TAG, addProjectBuildStageCommand));
                userMessageTransporterUseCase.broadcastBuildJobToUser(domainId);
            });
            protocolMessage.getSubApps().forEach(app -> {
                UpdateProjectAppCommand updateProjectAppCommand = new UpdateProjectAppCommand(app.getProjectId(), app.getBranch(), app.getAppName(), app.getAppPath());
                domainEventPublisher.publishEvent(KafkaTopic.DOMAIN_EVENT_TOPIC, DomainEvent.byData(domainId, ProjectDomainEventType.PROJECT_BUILD_UPDATE_SUB_APPS, updateProjectAppCommand));
            });
        });

        kubernetesApi.readLog(namespace, name, projectBuildMessageHandler::append);
    }
}
