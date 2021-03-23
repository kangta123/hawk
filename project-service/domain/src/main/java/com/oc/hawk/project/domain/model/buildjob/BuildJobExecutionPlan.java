package com.oc.hawk.project.domain.model.buildjob;


import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.oc.hawk.ddd.DomainEntity;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@DomainEntity
@Getter
public class BuildJobExecutionPlan {
    private ProjectBuildState state;
    private List<ProjectBuildStage> stages;
    private Set<ProjectImage> projectImages;
    private ProjectBuildExecutionEnv env;

    private LocalDateTime endTime;

    public BuildJobExecutionPlan(ProjectBuildState state, List<ProjectBuildStage> stages, Set<ProjectImage> projectImages, ProjectBuildExecutionEnv env, LocalDateTime endTime) {
        this.state = state;
        this.stages = stages;
        this.projectImages = projectImages;
        this.env = env;
        this.endTime = endTime;
    }

    public BuildJobExecutionPlan(ProjectBuildExecutionEnv env) {
        this.env = env;
        this.state = ProjectBuildState.START;
    }

    public static BuildJobExecutionPlan createNew() {
        BuildJobExecutionPlan buildJobExecutionPlan = new BuildJobExecutionPlan(new ProjectBuildExecutionEnv(""));
        buildJobExecutionPlan.addNewStage(JobStage.Created, true, null);
        return buildJobExecutionPlan;
    }

    void addNewStage(JobStage jobStage, boolean isSuccess, ProjectImage image) {
        ProjectBuildStage projectBuildStage = ProjectBuildStage.builder()
            .stage(jobStage)
            .success(isSuccess)
            .createdAt(LocalDateTime.now())
            .data(image == null ? null : image.toString())
            .build();

        if (this.stages == null) {
            stages = Lists.newArrayList();
        }

        if (!isSuccess) {
            this.state = ProjectBuildState.FAILED;
            this.endTime = LocalDateTime.now();
        }

        if (jobStage == JobStage.End) {
            this.state = ProjectBuildState.END;
            this.endTime = LocalDateTime.now();
        }

        if (image != null) {
            if (this.projectImages == null) {
                projectImages = Sets.newHashSet();
            }
            projectImages.add(image);
        }

        if (this.state == ProjectBuildState.START && projectBuildStage.getStage() == JobStage.Start) {
            this.state = ProjectBuildState.RUNNING;
        }
        this.stages.add(projectBuildStage);
    }

    public void updateExecutionEnv(ProjectBuildExecutionEnv env) {
        this.env = env;
    }
}
