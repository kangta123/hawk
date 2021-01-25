package com.oc.hawk.project.domain.service;

import com.oc.hawk.api.exception.DomainNotFoundException;
import com.oc.hawk.project.domain.model.buildjob.ProjectBuildExecutionEnv;
import com.oc.hawk.project.domain.model.buildjob.ProjectBuildJob;
import com.oc.hawk.project.domain.model.buildjob.ProjectBuildJobID;
import com.oc.hawk.project.domain.model.buildjob.ProjectBuildJobRepository;
import com.oc.hawk.project.domain.model.codebase.CodeBase;
import com.oc.hawk.project.domain.model.codebase.CodeBaseRepository;
import com.oc.hawk.project.domain.model.gitrecord.GitCommitLogRepository;
import com.oc.hawk.project.domain.model.project.Project;
import com.oc.hawk.project.domain.model.project.ProjectAppRepository;
import com.oc.hawk.project.domain.model.project.ProjectID;
import com.oc.hawk.project.domain.model.project.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProjectBuildJobExecutionService {
    private final ProjectBuildJobRepository projectBuildJobRepository;
    private final ProjectRepository projectRepository;
    private final CodeBaseRepository codeBaseRepository;
    private final ProjectAppRepository projectAppRepository;
    private final GitCommitLogRepository gitCommitLogRepository;
    private ProjectBuildEnv projectBuildEnv;

    @PostConstruct
    public void setup() {
        projectBuildEnv = new DockerProjectBuildEnvService(projectAppRepository, gitCommitLogRepository);
    }


    public ProjectBuildJob readyProjectBuildJob(Long projectJobId) {
        Optional<ProjectBuildJob> buildJobOptional = projectBuildJobRepository.byId(new ProjectBuildJobID(projectJobId));

        ProjectBuildJob projectBuildJob = buildJobOptional
            .orElseThrow(() -> new DomainNotFoundException(projectJobId));


        ProjectID projectId = projectBuildJob.getProjectId();
        CodeBase codeBase = codeBaseRepository.byProjectId(projectId);
        Project project = projectRepository.byId(projectId);

        ProjectBuildExecutionEnv projectBuildExecutionEnv = projectBuildEnv.generateEnvVariable(projectBuildJob, project, codeBase);

        projectBuildJob.ready(projectBuildExecutionEnv);

        projectBuildJobRepository.save(projectBuildJob);
        return projectBuildJob;

    }
}
