package com.oc.hawk.kubernetes.runtime.port.driving.event;

import com.oc.hawk.api.constant.KafkaTopic;
import com.oc.hawk.container.api.command.CreateRuntimeInfoSpecCommand;
import com.oc.hawk.container.api.command.DeleteRuntimeInfoCommand;
import com.oc.hawk.container.api.command.StopRuntimeInfoCommand;
import com.oc.hawk.container.api.event.ProjectBuildRuntimeStopEvent;

import com.oc.hawk.ddd.event.DomainEvent;
import com.oc.hawk.kubernetes.api.constants.RuntimeInfoDTO;
import com.oc.hawk.kubernetes.runtime.application.runtime.KubernetesProjectBuildExecutorUseCase;
import com.oc.hawk.kubernetes.runtime.application.runtime.KubernetesRuntimeExecutorUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.oc.hawk.container.api.event.RuntimeDomainEventType.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class KubernetesRuntimeConsumer {
    private final KubernetesRuntimeExecutorUseCase kubernetesRuntimeExecutorUseCase;
    private final KubernetesProjectBuildExecutorUseCase kubernetesProjectBuildExecutorUseCase;

    @KafkaListener(topics = KafkaTopic.INFRASTRUCTURE_RESOURCE_TOPIC)
    public void createDeployment(DomainEvent event) {
        log.info("Received event {}", event);
        if (event.is(RUNTIME_START_EVENT)) {
            CreateRuntimeInfoSpecCommand data = (CreateRuntimeInfoSpecCommand) event.getData();
            kubernetesRuntimeExecutorUseCase.start(event.getDomainId(), data);
        }
        if (event.is(RUNTIME_DELETE_EVENT)) {
            DeleteRuntimeInfoCommand command = (DeleteRuntimeInfoCommand) event.getData();
            kubernetesRuntimeExecutorUseCase.delete(command);
        }
        if (event.is(RUNTIME_STOP_EVENT)) {
            StopRuntimeInfoCommand command = (StopRuntimeInfoCommand) event.getData();
            kubernetesRuntimeExecutorUseCase.stop(command);
        }

        if (event.is(RUNTIME_BUILD_STOP_EVENT)) {
            ProjectBuildRuntimeStopEvent eventData = (ProjectBuildRuntimeStopEvent) event.getData();
            kubernetesProjectBuildExecutorUseCase.stop(eventData);
        }

        if (event.is(RUNTIME_STATE_UPDATED_EVENT)) {
            RuntimeInfoDTO eventData = (RuntimeInfoDTO) event.getData();
            kubernetesRuntimeExecutorUseCase.checkRuntimeStatus(eventData);
        }
    }
}
