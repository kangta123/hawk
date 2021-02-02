package com.oc.hawk.project.port.driven.persistence.po;


import com.oc.hawk.common.hibernate.BaseEntity;
import com.oc.hawk.project.domain.model.codebase.CodeBaseID;
import com.oc.hawk.project.domain.model.project.*;
import com.oc.hawk.project.domain.model.projectapp.ProjectApp;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Set;

@Table(name = "project_info")
@Getter
@Setter
@Entity
public class ProjectPO extends BaseEntity {
    private String name;
    private String descn;

    private String type;
    private String command;

    private String projectRuntime;
    private Long codeBaseId;
    private String groupName;
    private Long departmentId;
    private String mode;

    private String output;
    private LocalDateTime createdTime = LocalDateTime.now();

    public static ProjectPO createBy(Project project) {
        ProjectPO projectPo = new ProjectPO();
        if (project.getProjectId() != null) {
            projectPo.setId(project.getProjectId().getId());
        }
        projectPo.setCodeBaseId(project.getCodeBaseId().getId());
        projectPo.setMode(project.getMode() != null ? String.valueOf(project.getMode()) : null);
        projectPo.setCommand(project.buildCommand());
        projectPo.setType(String.valueOf(project.buildType()));
        projectPo.setCreatedTime(project.getCreatedTime());
        projectPo.setDepartmentId(project.getDepartmentId());
        projectPo.setDescn(project.getDescn());
        projectPo.setName(String.valueOf(project.getName()));
        projectPo.setProjectRuntime(String.valueOf(project.getRuntime()));
        projectPo.setGroupName(String.valueOf(project.getGroupName()));
        projectPo.setOutput(project.getBuild().getOutput());

        return projectPo;
    }

    public Project toProject() {
        return toProject(null);
    }

    public Project toProject(Set<ProjectApp> apps) {
        return Project.builder()
            .codeBaseId(new CodeBaseID(codeBaseId))
            .createdTime(createdTime)
            .departmentId(departmentId)
            .mode(mode != null ? ProjectBuildMode.valueOf(mode) : null)
            .descn(descn)
            .name(new ProjectName(name))
            .build(new ProjectBuild(BuildType.valueOf(type), new BuildCommand(command), this.output))
            .runtime(ProjectRuntime.valueOf(projectRuntime))
            .projectId(new ProjectID(getId()))
            .groupName(groupName)
            .apps(apps)
            .build();
    }
}

