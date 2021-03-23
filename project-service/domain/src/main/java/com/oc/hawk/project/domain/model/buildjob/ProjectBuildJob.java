package com.oc.hawk.project.domain.model.buildjob;

import com.oc.hawk.ddd.AggregateRoot;
import com.oc.hawk.project.domain.model.codebase.git.GitCommitLogID;
import com.oc.hawk.project.domain.model.project.ProjectID;
import com.oc.hawk.project.domain.model.projectapp.ProjectAppID;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@AggregateRoot
@Getter
@Builder
public class ProjectBuildJob {
    private final ProjectBuildJobID id;
    private final ProjectID projectId;
    private final ProjectImageTag imageTag;
    private final LocalDateTime createdAt;
    private final JobCreator creator;
    private final List<ProjectAppID> subApps;
    private final BuildJobExecutionPlan executionPlan;
    private final GitCommitLogID gitCommitLogId;

    public void ready(ProjectBuildExecutionEnv env) {
        this.executionPlan.updateExecutionEnv(env);
        executionPlan.addNewStage(JobStage.Ready, true, null);
    }

    public String imageTagStr() {
        return this.imageTag.getTag();
    }

    public void addNewStage(JobStage jobStage, boolean isSuccess, ProjectImage image) {
        executionPlan.addNewStage(jobStage, isSuccess, image);
    }

    public boolean isEnd() {
        ProjectBuildState state = this.executionPlan.getState();
        return state == ProjectBuildState.END;
    }

    public boolean isFailed() {
        ProjectBuildState state = this.executionPlan.getState();
        return state == ProjectBuildState.FAILED;
    }

    public void endJob() {
        executionPlan.addNewStage(JobStage.End, true, null);
    }

}

