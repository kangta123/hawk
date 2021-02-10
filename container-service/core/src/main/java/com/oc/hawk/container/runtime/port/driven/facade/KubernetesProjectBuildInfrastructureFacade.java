package com.oc.hawk.container.runtime.port.driven.facade;

import com.oc.hawk.container.api.command.CreateRuntimeInfoSpecCommand;
import com.oc.hawk.container.api.event.ContainerDomainEventType;
import com.oc.hawk.container.api.event.ProjectBuildRuntimeStopEvent;
import com.oc.hawk.container.domain.config.ContainerConfiguration;
import com.oc.hawk.container.domain.model.runtime.build.ProjectBuildLabel;
import com.oc.hawk.container.runtime.application.representation.InstanceRuntimeRepresentation;
import com.oc.hawk.container.runtime.common.facade.ProjectBuildInfrastructureFacade;
import com.oc.hawk.ddd.event.DomainEvent;
import com.oc.hawk.ddd.event.EventPublisher;
import com.oc.hawk.project.api.dto.ProjectBuildReadyDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class KubernetesProjectBuildInfrastructureFacade implements ProjectBuildInfrastructureFacade {
    private final EventPublisher domainEventPublisher;
    private final ContainerConfiguration containerConfiguration;
    private final InstanceRuntimeRepresentation instanceRuntimeRepresentation;

    @Override
    public void createBuildRuntime(Long buildJobId, ProjectBuildReadyDTO data) {
        log.info("Create build runtime Pod, {}", buildJobId);
        CreateRuntimeInfoSpecCommand spec = instanceRuntimeRepresentation.buildRuntimeSpecCommand(buildJobId, data);

        domainEventPublisher.publishDomainEvent(DomainEvent.byData(buildJobId, ContainerDomainEventType.INSTANCE_STARTED, spec));
    }

    @Override
    public void stopBuildRuntime(Long domainId) {
        log.info("Stop build runtime pod, {}", domainId);
        ProjectBuildRuntimeStopEvent data = ProjectBuildRuntimeStopEvent.ofLabel(containerConfiguration.getBuildNamespace(), ProjectBuildLabel.stopBuildLabels(domainId));
        domainEventPublisher.publishDomainEvent(DomainEvent.byData(domainId, ContainerDomainEventType.BUILD_RUNTIME_STOPPED, data));
    }
}
