package com.oc.hawk.project.port.driven.persistence.po;

import com.oc.hawk.common.hibernate.BaseEntity;
import com.oc.hawk.project.domain.model.buildjob.JobStage;
import com.oc.hawk.project.domain.model.buildjob.ProjectBuildStage;
import com.oc.hawk.project.domain.model.buildjob.ProjectBuildStageID;
import com.oc.hawk.project.domain.model.buildjob.ProjectImage;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@Table(name = "project_build_job_stage")
@Entity
public class ProjectBuildStagePO extends BaseEntity {
    private Long jobId;
    private boolean success;
    private LocalDateTime startTime;
    private String title;
    private String data;
    private String jobStage;

    public static ProjectBuildStagePO createdBy(Long jobId, ProjectBuildStage projectBuildStage) {
        ProjectBuildStagePO projectBuildStagePo = new ProjectBuildStagePO();
        if (projectBuildStage.getId() != null) {
            projectBuildStagePo.setId(projectBuildStage.getId().getId());
        }

        projectBuildStagePo.setData(projectBuildStage.getData());
        projectBuildStagePo.setJobId(jobId);
        projectBuildStagePo.setSuccess(projectBuildStage.isSuccess());
        projectBuildStagePo.setStartTime(projectBuildStage.getCreatedAt());

        JobStage stage = projectBuildStage.getStage();
        projectBuildStagePo.setJobStage(String.valueOf(stage));
        projectBuildStagePo.setTitle(stage.getTitle());

        return projectBuildStagePo;
    }

    public ProjectBuildStage toProjectBuildStage() {
        JobStage stage = JobStage.valueOf(jobStage);
        return ProjectBuildStage.builder()
            .id(new ProjectBuildStageID(getId()))
            .stage(stage)
            .success(success)
            .createdAt(startTime)
            .data(data).build();
    }


    public ProjectImage getProjectImage() {
        if (StringUtils.isEmpty(data)) {
            return null;
        }
        return new ProjectImage(data);
    }
}
