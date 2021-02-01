package com.oc.hawk.container.runtime.port.driving.event;

import com.oc.hawk.api.constant.KafkaTopic;
import com.oc.hawk.container.api.dto.InstanceConfigDTO;
import com.oc.hawk.container.api.event.ContainerDomainEventType;
import com.oc.hawk.container.runtime.application.instance.InstanceExecutorUseCase;
import com.oc.hawk.container.runtime.application.project.ProjectBuildJobUseCase;
import com.oc.hawk.ddd.event.DomainEvent;
import com.oc.hawk.project.api.dto.ProjectBuildStartDTO;
import com.oc.hawk.project.api.event.ProjectDomainEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@RequiredArgsConstructor
public class DomainEventConsumer {
    private final ProjectBuildJobUseCase projectBuildJobUseCase;
    private final InstanceExecutorUseCase instanceExecutorUseCase;

    @KafkaListener(topics = {KafkaTopic.DOMAIN_EVENT_TOPIC})
    public void serviceUpdated(DomainEvent event) {
        log.info("Domain event {} consumed", event);
        if (event.is(ProjectDomainEventType.PROJECT_BUILD_JOB_END)) {
            projectBuildJobUseCase.endBuildJob(event.getDomainId());
        }

        if (event.is(ProjectDomainEventType.PROJECT_BUILD_JOB_READY)) {
            ProjectBuildStartDTO data = (ProjectBuildStartDTO) event.getData();
            projectBuildJobUseCase.startBuildJob(event.getDomainId(), data);
        }

        if (event.is(ContainerDomainEventType.INSTANCE_DELETED)) {
            InstanceConfigDTO configDTO = (InstanceConfigDTO) event.getData();
            instanceExecutorUseCase.deleteService(configDTO.getName(), configDTO.getServiceName(), configDTO.getNamespace());
        }
        if (event.is(ProjectDomainEventType.PROJECT_DELETED)) {
            instanceExecutorUseCase.deleteServiceByProject(event.getDomainId());
        }
    }
}
