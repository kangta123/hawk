package com.oc.hawk.project.port.driven.persistence.po;

import com.oc.hawk.common.hibernate.BaseEntity;
import com.oc.hawk.project.domain.model.buildjob.ProjectBuildJobID;
import com.oc.hawk.project.domain.model.buildjob.ProjectBuildPost;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Table(name = "project_build_post")
@Entity
public class ProjectBuildPostPO extends BaseEntity {
    private long projectBuildId;
    private Long instanceId;
    private String instanceName;

    public static ProjectBuildPostPO create(ProjectBuildPost projectBuildPost) {
        final ProjectBuildPostPO projectBuildPostPo = new ProjectBuildPostPO();
        projectBuildPostPo.setInstanceId(projectBuildPost.getDeployTo());
        projectBuildPostPo.setProjectBuildId(projectBuildPost.getProjectBuildJobId().getId());
        projectBuildPostPo.setInstanceName(projectBuildPost.getDeployToInstance());
        return projectBuildPostPo;
    }

    public ProjectBuildPost toBuildPost() {
        return new ProjectBuildPost(instanceId, new ProjectBuildJobID(projectBuildId), instanceName);
    }
}
