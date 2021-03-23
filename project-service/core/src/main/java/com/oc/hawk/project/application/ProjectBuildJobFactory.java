package com.oc.hawk.project.application;

import com.oc.hawk.project.api.command.CreateProjectBuildJobCommand;
import com.oc.hawk.project.domain.facade.UserFacade;
import com.oc.hawk.project.domain.model.buildjob.*;
import com.oc.hawk.project.domain.model.codebase.git.GitCommitLogID;
import com.oc.hawk.project.domain.model.project.ProjectID;
import com.oc.hawk.project.domain.model.projectapp.ProjectAppID;
import com.oc.hawk.project.domain.model.user.UserInfo;
import com.oc.hawk.project.domain.service.AutoIncrementTagGenerator;
import com.oc.hawk.project.domain.service.CustomTagGenerator;
import com.oc.hawk.project.domain.service.ProjectImageTagGenerator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProjectBuildJobFactory {
    private final ProjectBuildJobRepository projectBuildJobRepository;
    private final UserFacade userFacade;

    public ProjectBuildPost createProjectBuildPost(CreateProjectBuildJobCommand addProjectBuildJobCommand, ProjectBuildJobID projectBuildJobId) {
        return new ProjectBuildPost(addProjectBuildJobCommand.getConfig(), projectBuildJobId, addProjectBuildJobCommand.getConfigName());
    }

    public ProjectBuildJob createProjectBuildJob(CreateProjectBuildJobCommand addProjectBuildJobCommand, GitCommitLogID commitLogId) {

        ProjectID projectId = new ProjectID(addProjectBuildJobCommand.getProjectId());

        ProjectImageTagGenerator projectImageTagGenerator = getProjectImageTagGenerator(addProjectBuildJobCommand.getVersion());
        ProjectImageTag imageTag = projectImageTagGenerator.createImageTag(projectId, addProjectBuildJobCommand.getVersion());


        List<ProjectAppID> subApps = null;
        if (addProjectBuildJobCommand.getAppIds() != null) {
            subApps = addProjectBuildJobCommand.getAppIds().stream().map(ProjectAppID::new).collect(Collectors.toList());
        }
        return ProjectBuildJob.builder()
            .projectId(projectId)
            .imageTag(imageTag)
            .createdAt(LocalDateTime.now())
            .creator(getJobCreator())
            .subApps(subApps)
            .gitCommitLogId(commitLogId)
            .executionPlan(BuildJobExecutionPlan.createNew())
            .build();
    }


    private JobCreator getJobCreator() {
        UserInfo userInfo = userFacade.currentUser();
        return new JobCreator(new UserID(userInfo.getUserId()), userInfo.getUserName());
    }

    private ProjectImageTagGenerator getProjectImageTagGenerator(String version) {
        if (StringUtils.isEmpty(version)) {
            return new AutoIncrementTagGenerator(projectBuildJobRepository);
        } else {
            return new CustomTagGenerator(projectBuildJobRepository);
        }
    }


}
