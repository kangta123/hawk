package com.oc.hawk.kubernetes.runtime.application.runtime;

import com.oc.hawk.api.constant.KafkaTopic;
import com.oc.hawk.common.spring.cloud.stream.event.KafkaEventPublish;
import com.oc.hawk.container.api.command.CreateRuntimeInfoSpecCommand;
import com.oc.hawk.container.api.command.DeleteRuntimeInfoCommand;
import com.oc.hawk.container.api.command.StopRuntimeInfoCommand;
import com.oc.hawk.container.api.event.ContainerDomainEventType;
import com.oc.hawk.container.api.event.EntryPointUpdatedEvent;
import com.oc.hawk.ddd.event.DomainEvent;
import com.oc.hawk.infrastructure.application.KubernetesApi;
import com.oc.hawk.kubernetes.api.constants.RuntimeInfoDTO;
import com.oc.hawk.kubernetes.api.dto.RuntimeStartFailedDto;
import com.oc.hawk.kubernetes.domain.model.RuntimeRestartCountExceededException;
import com.oc.hawk.kubernetes.domain.service.RuntimeInspectionService;
import com.oc.hawk.kubernetes.runtime.application.entrypoint.ServiceEntryPointCreator;
import com.oc.hawk.kubernetes.runtime.application.runtime.spec.BuildRuntimeSpecFactory;
import com.oc.hawk.kubernetes.runtime.application.runtime.spec.NetworkConfigSpec;
import com.oc.hawk.kubernetes.runtime.application.runtime.spec.RuntimeConfigSpec;
import com.oc.hawk.kubernetes.runtime.application.runtime.starter.KubernetesServiceStarterFactory;
import com.oc.hawk.kubernetes.runtime.application.runtime.starter.ServiceRuntimeStarter;
import io.fabric8.kubernetes.client.KubernetesClientException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.oc.hawk.container.api.event.ContainerDomainEventType.RUNTIME_STARTED;
import static com.oc.hawk.container.api.event.ContainerDomainEventType.RUNTIME_START_FAILED;

@Component
@RequiredArgsConstructor
@Slf4j
public class KubernetesRuntimeExecutorUseCase {
    private final BuildRuntimeSpecFactory buildRuntimeSpecFactory;
    private final KafkaEventPublish kafkaDomainEventPublish;
    private final KubernetesApi kubernetesApi;
    private final ServiceEntryPointCreator serviceEntryPointCreator;

    public void start(Long domainId, CreateRuntimeInfoSpecCommand spec) {
        log.info("Start Normal runtime {} in {}", spec.getName(), spec.getNamespace());
        RuntimeConfigSpec configuration = buildRuntimeSpecFactory.toRuntimeConfiguration(spec);
        NetworkConfigSpec networkConfigSpec = configuration.getNetworkConfigSpec();
        if (networkConfigSpec != null) {
            EntryPointUpdatedEvent serviceEntryPoint = serviceEntryPointCreator.createServiceEntryPoint(configuration.getNamespace(), configuration.getProjectId(), networkConfigSpec);
            if (serviceEntryPoint != null) {
                kafkaDomainEventPublish.publishEvent(KafkaTopic.INFRASTRUCTURE_RESOURCE_TOPIC, DomainEvent.byData(domainId, ContainerDomainEventType.RUNTIME_ENTRYPOINT_UPDATED, serviceEntryPoint));
            }
        }

        ServiceRuntimeStarter starter = KubernetesServiceStarterFactory.starter(configuration);
        if (starter == null) {
            log.warn("No suitable starter , {}", spec.getName());
            notifyRuntimeStartFailed(spec);
            return;
        }

        try {
            starter.start(configuration);
        } catch (KubernetesClientException e) {
            log.error("Start service failed", e);
            notifyRuntimeStartFailed(spec);
        }

        kafkaDomainEventPublish.publishEvent(KafkaTopic.INFRASTRUCTURE_RESOURCE_TOPIC, DomainEvent.byData(domainId, RUNTIME_STARTED, spec));
    }


    public void stop(StopRuntimeInfoCommand command) {
        log.info("Stop business runtime {} in {}", command.getName(), command.getNamespace());
        kubernetesApi.deleteDeployments(command.getNamespace(), command.getName());
    }

    public void delete(DeleteRuntimeInfoCommand command) {
        log.info("Delete service {}, in {}", command.getName(), command.getNamespace());
        kubernetesApi.deleteDeployments(command.getNamespace(), command.getName());
        kubernetesApi.deleteService(command.getName(), command.getNamespace());
    }

    public void checkRuntimeStatus(RuntimeInfoDTO eventData) {
        final RuntimeInspectionService runtimeInspectionService = new RuntimeInspectionService();
        try {
            runtimeInspectionService.checkRuntimeStatus(eventData.getRestartCount());
        } catch (RuntimeRestartCountExceededException e) {
            log.info("Runtime {} restart count {} exceeded limitation", eventData.getName(), eventData.getRestartCount());
            kubernetesApi.deleteDeployments(eventData.getNamespace(), eventData.getName());
            notifyRuntimeStartFailed(new RuntimeStartFailedDto(Long.parseLong(eventData.getProjectId()), eventData.getName(), eventData.getNamespace()));
        }
    }

    private void notifyRuntimeStartFailed(CreateRuntimeInfoSpecCommand spec) {
        final RuntimeStartFailedDto runtimeStartFailedDto = new RuntimeStartFailedDto(spec.getProjectId(), spec.getName(), spec.getNamespace());
        notifyRuntimeStartFailed(runtimeStartFailedDto);
    }

    private void notifyRuntimeStartFailed(RuntimeStartFailedDto runtimeStartFailedDto) {
        kafkaDomainEventPublish.publishEvent(KafkaTopic.INFRASTRUCTURE_RESOURCE_TOPIC, DomainEvent.byData(RUNTIME_START_FAILED, runtimeStartFailedDto));
    }
}
