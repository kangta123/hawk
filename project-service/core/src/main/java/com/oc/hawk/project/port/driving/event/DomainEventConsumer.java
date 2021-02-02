package com.oc.hawk.project.port.driving.event;

import com.oc.hawk.api.constant.KafkaTopic;
import com.oc.hawk.ddd.event.DomainEvent;
import com.oc.hawk.project.api.command.AddProjectBuildStageCommand;
import com.oc.hawk.project.api.command.UpdateProjectAppCommand;
import com.oc.hawk.project.api.event.ProjectDomainEventType;
import com.oc.hawk.project.application.ProjectAppUseCase;
import com.oc.hawk.project.application.ProjectBuildExecutionPlanUseCase;
import com.oc.hawk.project.application.ProjectBuildJobUseCase;
import com.oc.hawk.project.application.ProjectUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DomainEventConsumer {
    private final ProjectBuildJobUseCase projectBuildJobUseCase;
    private final ProjectBuildExecutionPlanUseCase executionPlanUseCase;
    private final ProjectAppUseCase projectAppUseCase;
    private final ProjectUseCase projectUseCase;

    @KafkaListener(topics = {KafkaTopic.DOMAIN_EVENT_TOPIC})
    public void buildJob(DomainEvent event) {
        log.info("Domain event {} consumed", event);

        if (event.is(ProjectDomainEventType.PROJECT_CREATED)) {
            projectUseCase.asyncUpdateGitRepo(event.getDomainId());
        }
        if (event.is(ProjectDomainEventType.PROJECT_BUILD_JOB_CREATED)) {
            Long domainId = event.getDomainId();
            projectBuildJobUseCase.executeProjectBuildJob(domainId);
        }
        if (event.is(ProjectDomainEventType.PROJECT_BUILD_UPDATE_TAG)) {
            Long jobId = event.getDomainId();
            AddProjectBuildStageCommand command = (AddProjectBuildStageCommand) event.getData();
            executionPlanUseCase.addJobStage(jobId, command);
        }
        if (event.is(ProjectDomainEventType.PROJECT_BUILD_UPDATE_SUB_APPS)) {
            UpdateProjectAppCommand command = (UpdateProjectAppCommand) event.getData();
            projectAppUseCase.updateProjectApp(command);
        }
    }
}
