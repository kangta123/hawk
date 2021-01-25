package com.oc.hawk.container.runtime.port.driven.facade;

import com.oc.hawk.api.constant.KafkaTopic;
import com.oc.hawk.container.api.command.CreateInstanceVolumeSpecCommand;
import com.oc.hawk.container.api.command.CreateRuntimeInfoSpecCommand;
import com.oc.hawk.container.api.command.CreateProjectBuildWatchLogCommand;
import com.oc.hawk.container.api.event.RuntimeDomainEventType;
import com.oc.hawk.container.api.event.ProjectBuildRuntimeStopEvent;
import com.oc.hawk.container.domain.config.ContainerConfiguration;
import com.oc.hawk.container.domain.model.project.ProjectRuntimeConfig;
import com.oc.hawk.container.runtime.application.project.representation.ProjectBuildRuntimeRepresentation;
import com.oc.hawk.container.runtime.common.facade.ProjectBuildInfrastructureFacade;
import com.oc.hawk.ddd.event.DomainEvent;
import com.oc.hawk.ddd.event.EventPublisher;
import com.oc.hawk.project.api.dto.ProjectBuildStartDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.oc.hawk.container.runtime.application.project.representation.ProjectBuildRuntimeRepresentation.LABEL_BUILD_JOB_ID;


@Component
@RequiredArgsConstructor
@Slf4j
public class KubernetesProjectBuildInfrastructureFacade implements ProjectBuildInfrastructureFacade {
    private final EventPublisher domainEventPublisher;
    private final ProjectBuildRuntimeRepresentation projectBuildRuntimeRepresentation;
    private final ContainerConfiguration containerConfiguration;


    @Override
    public void createBuildRuntime(Long buildJobId, ProjectBuildStartDTO data, ProjectRuntimeConfig projectRuntimeConfig) {
        log.info("Create build runtime Pod, {}", buildJobId);
        CreateRuntimeInfoSpecCommand spec = projectBuildRuntimeRepresentation.buildServiceRuntimeSpecDTO(buildJobId, data, projectRuntimeConfig);
        List<CreateInstanceVolumeSpecCommand> volumes = spec.getVolume();
        volumes.add(new CreateInstanceVolumeSpecCommand("/usr/bin/docker:/usr/bin/docker", "docker-volume", true));
        volumes.add(new CreateInstanceVolumeSpecCommand("/var/run/docker.sock:/var/run/docker.sock", "docker-sock-volume", true));
        domainEventPublisher.publishEvent(KafkaTopic.INFRASTRUCTURE_RESOURCE_TOPIC, DomainEvent.byData(buildJobId, RuntimeDomainEventType.RUNTIME_START_EVENT, spec));
    }

    @Override
    public void watchLog(long domainId, CreateRuntimeInfoSpecCommand spec) {
        CreateProjectBuildWatchLogCommand createProjectBuildWatchLogCommand = new CreateProjectBuildWatchLogCommand();
        createProjectBuildWatchLogCommand.setDomainId(domainId);
        createProjectBuildWatchLogCommand.setName(spec.getName());
        createProjectBuildWatchLogCommand.setNamespace(spec.getNamespace());

        domainEventPublisher.publishEvent(KafkaTopic.INFRASTRUCTURE_RESOURCE_TOPIC, DomainEvent.byData(null, RuntimeDomainEventType.RUNTIME_WATCH_LOG_EVENT, createProjectBuildWatchLogCommand));

    }

    @Override
    public void stopBuildRuntime(Long domainId) {
        log.info("Stop build runtime pod, {}", domainId);
        ProjectBuildRuntimeStopEvent data = ProjectBuildRuntimeStopEvent.ofLabel(containerConfiguration.getBuildNamespace(), LABEL_BUILD_JOB_ID, String.valueOf(domainId));
        domainEventPublisher.publishEvent(KafkaTopic.INFRASTRUCTURE_RESOURCE_TOPIC, DomainEvent.byData(domainId, RuntimeDomainEventType.RUNTIME_BUILD_STOP_EVENT, data));
    }
}
