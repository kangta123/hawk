package com.oc.hawk.message.port.driving.event;

import com.oc.hawk.api.constant.KafkaTopic;
import com.oc.hawk.container.api.dto.InstanceDeploymentDTO;
import com.oc.hawk.container.api.event.RuntimeDomainEventType;
import com.oc.hawk.ddd.event.DomainEvent;
import com.oc.hawk.message.application.EventMessageUseCase;
import com.oc.hawk.message.domain.model.EventType;
import com.oc.hawk.project.api.event.ProjectDomainEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class EventMessageConsumer {
    private final EventMessageUseCase eventMessageUseCase;


    @KafkaListener(topics = {KafkaTopic.DOMAIN_EVENT_TOPIC, KafkaTopic.INFRASTRUCTURE_RESOURCE_TOPIC})
    public void userMessageListener(DomainEvent domainEvent) {
        //Project build
        if(domainEvent.is(ProjectDomainEventType.PROJECT_BUILD_JOB_CREATED)){
            eventMessageUseCase.createEventMessage(EventType.PROJECT_BUILD,  domainEvent);
        }
        if(domainEvent.is(ProjectDomainEventType.PROJECT_CREATED)){
            eventMessageUseCase.createEventMessage(EventType.PROJECT_ADD, domainEvent);
        }
        if(domainEvent.is(ProjectDomainEventType.PROJECT_BUILD_JOB_FAILED)){
            eventMessageUseCase.createEventMessage(EventType.PROJECT_BUILD_FAILED,domainEvent);
        }
        if(domainEvent.is(RuntimeDomainEventType.RUNTIME_START_EVENT)){
            eventMessageUseCase.createEventMessage(EventType.RUNTIME_START, domainEvent);
        }
        if(domainEvent.is(RuntimeDomainEventType.RUNTIME_STOP_EVENT)){
            eventMessageUseCase.createEventMessage(EventType.RUNTIME_STOP, domainEvent);
        }
        if(domainEvent.is(RuntimeDomainEventType.INSTANCE_CONFIG_CREATED)){
            eventMessageUseCase.createEventMessage(EventType.RUNTIME_ADD_CONFIG, domainEvent);
        }
        if(domainEvent.is(RuntimeDomainEventType.INSTANCE_CONFIG_UPDATED)){
            eventMessageUseCase.createEventMessage(EventType.RUNTIME_UPDATE_CONFIG, domainEvent);
        }
        if(domainEvent.is(RuntimeDomainEventType.INSTANCE_CONFIG_DELETED)){
            eventMessageUseCase.createEventMessage(EventType.RUNTIME_DELETE_CONFIG, domainEvent);
        }
        if(domainEvent.is(RuntimeDomainEventType.INSTANCE_BUILD_AUTH_DEPLOY)){
            final InstanceDeploymentDTO data = (InstanceDeploymentDTO) domainEvent.getData();
            if(data.getProjectBuildJobId() != null){
                eventMessageUseCase.createEventMessage(EventType.RUNTIME_AUTH_DEPLOYMENT_BY_PROJECT_BUILD, domainEvent);
            }
        }
    }
}
