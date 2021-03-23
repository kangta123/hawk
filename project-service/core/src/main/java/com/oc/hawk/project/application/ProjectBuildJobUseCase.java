package com.oc.hawk.project.application;

import com.oc.hawk.api.exception.DomainNotFoundException;
import com.oc.hawk.ddd.event.DomainEvent;
import com.oc.hawk.ddd.event.EventPublisher;
import com.oc.hawk.project.api.command.CreateProjectBuildJobCommand;
import com.oc.hawk.project.api.dto.InstanceImageDTO;
import com.oc.hawk.project.api.dto.ProjectBuildJobDTO;
import com.oc.hawk.project.api.dto.ProjectBuildJobDetailDTO;
import com.oc.hawk.project.api.dto.ProjectBuildReadyDTO;
import com.oc.hawk.project.api.event.ProjectDomainEventType;
import com.oc.hawk.project.application.representation.ProjectBuildJobRepresentation;
import com.oc.hawk.project.domain.model.buildjob.*;
import com.oc.hawk.project.domain.model.codebase.git.GitCommitLogID;
import com.oc.hawk.project.domain.model.project.Project;
import com.oc.hawk.project.domain.model.project.ProjectBuild;
import com.oc.hawk.project.domain.model.project.ProjectID;
import com.oc.hawk.project.domain.model.project.ProjectRepository;
import com.oc.hawk.project.domain.service.ProjectBuildJobExecutionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProjectBuildJobUseCase {
    private final ProjectBuildJobFactory projectBuildJobFactory;
    private final ProjectBuildJobRepository projectBuildJobRepository;
    private final EventPublisher domainEventPublisher;
    private final ProjectBuildJobExecutionService projectBuildJobExecutionService;
    private final ProjectBuildJobRepresentation projectBuildJobRepresentation;
    private final ProjectRepository projectRepository;
    private final GitCommitLogUseCase gitCommitLogUseCase;
    private final ProjectBuildPostRepository projectBuildPostRepository;


    @Transactional(rollbackFor = Exception.class)
    public ProjectBuildJobDTO createProjectBuildJob(CreateProjectBuildJobCommand createProjectBuildJobCommand) {
        log.info("create project build from project {}", createProjectBuildJobCommand.getProjectId());
        Long projectId = createProjectBuildJobCommand.getProjectId();
        GitCommitLogID id = gitCommitLogUseCase.getLatestGitCommitLog(new ProjectID(projectId), createProjectBuildJobCommand.getBranch());


        ProjectBuildJob projectBuildJob = projectBuildJobFactory.createProjectBuildJob(createProjectBuildJobCommand, id);
        projectBuildJob = projectBuildJobRepository.save(projectBuildJob);

        final ProjectBuildJobID projectBuildJobId = projectBuildJob.getId();
        createProjectBuildJobPost(createProjectBuildJobCommand, projectBuildJobId);

        ProjectBuildJobDTO projectBuildJobDTO = projectBuildJobRepresentation.toProjectBuildJobDTO(projectBuildJob, new ProjectBuildJobDetailDTO());

        domainEventPublisher.publishDomainEvent(DomainEvent.byData(projectBuildJob.getId().getId(), ProjectDomainEventType.PROJECT_BUILD_JOB_CREATED, projectBuildJobDTO));
        return projectBuildJobDTO;
    }

    private void createProjectBuildJobPost(CreateProjectBuildJobCommand createProjectBuildJobCommand, ProjectBuildJobID projectBuildJobId) {
        ProjectBuildPost projectBuildPost = projectBuildJobFactory.createProjectBuildPost(createProjectBuildJobCommand, projectBuildJobId);
        projectBuildPostRepository.save(projectBuildPost);
    }

    @Transactional(rollbackFor = Exception.class)
    public void executeProjectBuildJob(Long projectJobId) {
        log.info("execute project build from project {}", projectJobId);

        ProjectBuildJob projectBuildJob = projectBuildJobExecutionService.readyProjectBuildJob(projectJobId);

        ProjectBuildReadyDTO data = getBuildReadyEventData(projectJobId, projectBuildJob);

        domainEventPublisher.publishDomainEvent(DomainEvent.byData(projectJobId, ProjectDomainEventType.PROJECT_BUILD_JOB_READY, data));
    }

    public List<InstanceImageDTO> queryInstanceImages(long projectId, String tag) {
        Set<InstanceImageInfo> instanceImageInfo = projectBuildJobRepository.queryInstanceImages(projectId, tag);

        if (instanceImageInfo != null) {
            return instanceImageInfo.stream()
                .sorted()
                .map(i -> new InstanceImageDTO(i.getTag(), i.getApp(), i.getBranch(), i.getJobId(), i.getTime()))
                .collect(Collectors.toList());
        }
        return null;
    }

    public List<ProjectBuildJobDTO> queryBuildJobs(long projectId) {
        final int fetchSize = 10;
        List<ProjectBuildJob> projectBuildJobs = projectBuildJobRepository.queryLatestBuildJobs(projectId, fetchSize);
        return projectBuildJobs.stream().map(job -> projectBuildJobRepresentation.toProjectBuildJobDTO(job, new ProjectBuildJobDTO())).collect(Collectors.toList());
    }

    public ProjectBuildJobDTO getBuildJob(long jobId) {
        ProjectBuildJob projectBuildJob = projectBuildJobRepository.byId(new ProjectBuildJobID(jobId)).orElseThrow(() -> new DomainNotFoundException(jobId));

        return projectBuildJobRepresentation.toProjectBuildJobDTO(projectBuildJob, new ProjectBuildJobDetailDTO());
    }

    public void stopProjectBuildJob(long jobId) {
        log.info("stop project build with id {}", jobId);

        ProjectBuildJob projectBuildJob = projectBuildJobRepository.byId(new ProjectBuildJobID(jobId)).orElseThrow(() -> new DomainNotFoundException(jobId));

        projectBuildJob.endJob();

        projectBuildJobRepository.save(projectBuildJob);

        ProjectBuildJobDTO projectBuildJobDTO = projectBuildJobRepresentation.toProjectBuildJobDTO(projectBuildJob, new ProjectBuildJobDetailDTO());

        domainEventPublisher.publishDomainEvent(DomainEvent.byData(projectBuildJob.getId().getId(), ProjectDomainEventType.PROJECT_BUILD_JOB_END, projectBuildJobDTO));
    }

    public List<String> getAllBuildTags() {
        return Arrays.stream(JobStage.values()).map(JobStage::getTitle).collect(Collectors.toList());
    }

    private ProjectBuildReadyDTO getBuildReadyEventData(Long projectJobId, ProjectBuildJob projectBuildJob) {
        BuildJobExecutionPlan executionPlan = projectBuildJob.getExecutionPlan();
        Project project = projectRepository.byId(projectBuildJob.getProjectId());

        ProjectBuild build = project.getBuild();
        return ProjectBuildReadyDTO.builder()
            .env(executionPlan.getEnv().env())
            .runtimeType(project.getRuntime().toString())
            .buildType(build.getType().toString())
            .buildOutPut(build.getOutput())
            .projectName(project.getName().getName())
            .jobId(projectJobId)
            .projectId(projectJobId)
            .build();
    }
}
