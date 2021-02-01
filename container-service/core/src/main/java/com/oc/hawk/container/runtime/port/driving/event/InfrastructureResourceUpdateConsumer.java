package com.oc.hawk.container.runtime.port.driving.event;

import com.oc.hawk.api.constant.KafkaTopic;
import com.oc.hawk.container.api.command.CreateRuntimeInfoSpecCommand;
import com.oc.hawk.container.api.event.EntryPointUpdatedEvent;
import com.oc.hawk.container.api.event.ContainerDomainEventType;
import com.oc.hawk.container.domain.model.runtime.info.RuntimeCatalog;
import com.oc.hawk.container.runtime.application.instance.InstanceConfigUseCase;
import com.oc.hawk.container.runtime.application.project.ProjectBuildJobUseCase;
import com.oc.hawk.ddd.event.DomainEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class InfrastructureResourceUpdateConsumer {

    private final InstanceConfigUseCase instanceConfigUseCase;

    @KafkaListener(topics = {KafkaTopic.INFRASTRUCTURE_RESOURCE_TOPIC})
    public void stateUpdated(DomainEvent domainEvent) {
        log.info("Domain event {} consumed", domainEvent);
        if (domainEvent.is(ContainerDomainEventType.RUNTIME_ENTRYPOINT_UPDATED)) {
            EntryPointUpdatedEvent data = (EntryPointUpdatedEvent) domainEvent.getData();
            instanceConfigUseCase.updateExposePorts(data.getServiceName(), data.getNamespace(), data.getExposePorts());
        }
    }
}
