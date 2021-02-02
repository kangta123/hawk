package com.oc.hawk.project.port.driven.persistence.po;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.oc.hawk.common.hibernate.BaseEntity;
import com.oc.hawk.project.domain.model.buildjob.*;
import com.oc.hawk.project.domain.model.codebase.git.GitCommitLogID;
import com.oc.hawk.project.domain.model.project.ProjectID;
import com.oc.hawk.project.domain.model.projectapp.ProjectAppID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public
@Getter
@Setter
@Table(name = "project_build_job")
@Entity
@DynamicUpdate
class ProjectBuildJobPO extends BaseEntity {
    private static final String DELIMITER = ",";

    private Long projectId;
    private String tag;
    private String creatorName;
    private Long creator;
    private String state;
    private LocalDateTime createdTime;
    private LocalDateTime endTime;
    private String env;
    private String subApps;
    private Long commitLogId;

    public static ProjectBuildJobPO createdBy(ProjectBuildJob projectBuildJob) {
        ProjectBuildJobPO projectBuildJobPo = new ProjectBuildJobPO();
        projectBuildJobPo.setProjectId(projectBuildJob.getProjectId().getId());
        projectBuildJobPo.setTag(projectBuildJob.getImageTag().getTag());
        if (projectBuildJob.getGitCommitLogId() != null) {
            projectBuildJobPo.setCommitLogId(projectBuildJob.getGitCommitLogId().getId());
        }

        if (projectBuildJob.getCreatedAt() == null) {
            projectBuildJobPo.setCreatedTime(LocalDateTime.now());
        } else {
            projectBuildJobPo.setCreatedTime(projectBuildJob.getCreatedAt());
        }

        if (projectBuildJob.getId() != null) {
            projectBuildJobPo.setId(projectBuildJob.getId().getId());
        }

        configJobCreator(projectBuildJob, projectBuildJobPo);

        configSubApps(projectBuildJob, projectBuildJobPo);

        configExecutionPlan(projectBuildJob, projectBuildJobPo);

        return projectBuildJobPo;
    }

    private static void configExecutionPlan(ProjectBuildJob projectBuildJob, ProjectBuildJobPO projectBuildJobPo) {
        ProjectBuildExecutionPlan executionPlan = projectBuildJob.getExecutionPlan();
        if (executionPlan != null) {
            projectBuildJobPo.setEndTime(executionPlan.getEndTime());

            projectBuildJobPo.setEnv(executionPlan.getEnv().envStr());
            ProjectBuildState state = executionPlan.getState();
            if (state != null) {
                projectBuildJobPo.setState(state.toString());
            }
        }
    }

    private static void configSubApps(ProjectBuildJob projectBuildJob, ProjectBuildJobPO projectBuildJobPo) {
        List<ProjectAppID> subApps = projectBuildJob.getSubApps();
        if (subApps != null) {
            String subAppIds = subApps.stream().map(ProjectAppID::getId)
                .map(String::valueOf)
                .collect(Collectors.joining(DELIMITER));
            projectBuildJobPo.setSubApps(subAppIds);
        }
    }

    private static void configJobCreator(ProjectBuildJob projectBuildJob, ProjectBuildJobPO projectBuildJobPo) {
        JobCreator creator = projectBuildJob.getCreator();
        if (creator != null) {
            projectBuildJobPo.setCreator(creator.getCreateId().getId());
            projectBuildJobPo.setCreatorName(creator.getName());
        }
    }


    public ProjectBuildJob toProjectBuildJob(List<ProjectBuildStagePO> projectBuildStages) {
        List<ProjectAppID> subAppList = null;
        if (this.subApps != null) {
            subAppList = Stream.of(subApps.split(DELIMITER)).map(Long::valueOf).map(ProjectAppID::new).collect(Collectors.toList());
        }

        ProjectBuildState state = null;
        if (this.state != null) {
            state = ProjectBuildState.valueOf(this.state);
        }

        List<ProjectBuildStage> stages = Lists.newArrayList();
        Set<ProjectImage> images = Sets.newHashSet();
        if (projectBuildStages != null) {
            projectBuildStages.forEach(stage -> {
                stages.add(stage.toProjectBuildStage());
                ProjectImage image = stage.getProjectImage();
                if (image != null) {
                    images.add(image);
                }
            });
            Collections.sort(stages);
        }

        ProjectBuildExecutionPlan executionPlan = new ProjectBuildExecutionPlan(state, stages, images, new ProjectBuildExecutionEnv(this.getEnv()), this.getEndTime());
        GitCommitLogID commitLogId = null;
        if (this.getCommitLogId() != null) {
            commitLogId = new GitCommitLogID(this.getCommitLogId());
        }
        return ProjectBuildJob.builder()
            .id(new ProjectBuildJobID(getId()))
            .projectId(new ProjectID(this.getProjectId()))
            .imageTag(new ProjectImageTag(tag))
            .gitCommitLogId(commitLogId)
            .creator(new JobCreator(new UserID(getCreator()), this.getCreatorName()))
            .executionPlan(executionPlan)
            .createdAt(createdTime)
            .subApps(subAppList).build();
    }

}
