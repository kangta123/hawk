package com.oc.hawk.kubernetes.keepalive.port.driving.event;

import com.oc.hawk.api.constant.KafkaTopic;
import com.oc.hawk.container.api.command.CreateProjectBuildWatchLogCommand;
import com.oc.hawk.container.api.event.EntryPointUpdatedEvent;
import com.oc.hawk.ddd.event.DomainEvent;
import com.oc.hawk.kubernetes.api.constants.RuntimeInfoDTO;
import com.oc.hawk.kubernetes.api.dto.RuntimeStartFailedDto;
import com.oc.hawk.kubernetes.keepalive.application.KubernetesProjectBuildLogUseCase;
import com.oc.hawk.kubernetes.keepalive.application.KubernetesRuntimeUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.oc.hawk.container.api.event.RuntimeDomainEventType.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class KubernetesInfrastructureResourceConsumer {
    private final KubernetesRuntimeUseCase kubernetesRuntimeUseCase;
    private final KubernetesProjectBuildLogUseCase kubernetesProjectBuildLogUseCase;

    @KafkaListener(topics = {KafkaTopic.INFRASTRUCTURE_RESOURCE_TOPIC})
    public void stateUpdated(DomainEvent domainEvent) {
        log.info("Runtime state updated {}", domainEvent);
        if (domainEvent.is(RUNTIME_START_FAILED)) {
            final RuntimeStartFailedDto data = (RuntimeStartFailedDto) domainEvent.getData();
            kubernetesRuntimeUseCase.runtimeStartFailed(data);
        }
        if (domainEvent.is(RUNTIME_STATE_UPDATED_EVENT)) {
            RuntimeInfoDTO runtimeInfo = (RuntimeInfoDTO) domainEvent.getData();
            kubernetesRuntimeUseCase.updateState(runtimeInfo);
        }
        if (domainEvent.is(RUNTIME_ENTRYPOINT_UPDATED)) {
            EntryPointUpdatedEvent serviceEntryPoint = (EntryPointUpdatedEvent) domainEvent.getData();
            kubernetesRuntimeUseCase.updateEntryPoint(serviceEntryPoint);
        }
        if (domainEvent.is(RUNTIME_WATCH_LOG_EVENT)) {
            CreateProjectBuildWatchLogCommand executionSpecDTO = (CreateProjectBuildWatchLogCommand) domainEvent.getData();
            kubernetesProjectBuildLogUseCase.asyncWatchLog(executionSpecDTO.getNamespace(), executionSpecDTO.getName(), executionSpecDTO.getDomainId());
        }
    }
}
