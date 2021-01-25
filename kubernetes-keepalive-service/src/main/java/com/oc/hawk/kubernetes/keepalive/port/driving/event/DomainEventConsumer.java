package com.oc.hawk.kubernetes.keepalive.port.driving.event;

import com.oc.hawk.api.constant.KafkaTopic;
import com.oc.hawk.ddd.event.DomainEvent;
import com.oc.hawk.kubernetes.keepalive.application.UserMessageTransporterUseCase;
import com.oc.hawk.project.api.event.ProjectDomainEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DomainEventConsumer {
    private final UserMessageTransporterUseCase userMessageTransporterUseCase;

    @KafkaListener(topics = {KafkaTopic.DOMAIN_EVENT_TOPIC})
    public void consumeEvent(DomainEvent domainEvent) {
        log.info("Domain event {} consumed", domainEvent);

        if (domainEvent.is(ProjectDomainEventType.PROJECT_BUILD_JOB_END)) {
            userMessageTransporterUseCase.broadcastBuildJobToUser(domainEvent.getDomainId());
        }
    }
}
