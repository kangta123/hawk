package com.oc.hawk.project.application;

import com.oc.hawk.api.exception.DomainNotFoundException;
import com.oc.hawk.ddd.event.DomainEvent;
import com.oc.hawk.ddd.event.EventPublisher;
import com.oc.hawk.project.api.command.AddProjectBuildStageCommand;
import com.oc.hawk.project.api.dto.ProjectBuildJobDTO;
import com.oc.hawk.project.api.event.ProjectDomainEventType;
import com.oc.hawk.project.application.representation.ProjectBuildJobRepresentation;
import com.oc.hawk.project.domain.model.buildjob.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProjectBuildExecutionPlanUseCase {
    private final ProjectBuildJobRepository projectBuildJobRepository;
    private final EventPublisher domainEventPublisher;
    private final ProjectBuildJobRepresentation projectBuildJobRepresentation;

    @Transactional(rollbackFor = Exception.class)
    public void addJobStage(Long jobId, AddProjectBuildStageCommand command) {
        log.info("job {} add stage {}, success {}", jobId, command.getStage(), command.isSuccess());
        ProjectBuildJob projectBuildJob = projectBuildJobRepository.byId(new ProjectBuildJobID(jobId)).orElseThrow(() -> new DomainNotFoundException(jobId));
        JobStage jobStage = JobStage.valueOf(command.getStage());
        boolean success = command.isSuccess();
        ProjectImage image = new ProjectImage(command.getImage());

        projectBuildJob.addNewStage(jobStage, success, image);


        projectBuildJobRepository.save(projectBuildJob);


        ProjectBuildJobDTO projectBuildJobDTO = projectBuildJobRepresentation.toProjectBuildJobDTO(projectBuildJob);
        if (projectBuildJob.isEnd()) {
            domainEventPublisher.publishDomainEvent(DomainEvent.byData(jobId, ProjectDomainEventType.PROJECT_BUILD_JOB_END, projectBuildJobDTO));
        } else if (projectBuildJob.isFailed()) {
            domainEventPublisher.publishDomainEvent(DomainEvent.byData(jobId, ProjectDomainEventType.PROJECT_BUILD_JOB_FAILED, projectBuildJobDTO));
        }

    }
}
