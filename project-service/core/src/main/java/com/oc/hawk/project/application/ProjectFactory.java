package com.oc.hawk.project.application;

import com.oc.hawk.project.api.command.RegisterProjectCommand;
import com.oc.hawk.project.domain.facade.UserFacade;
import com.oc.hawk.project.domain.model.project.*;
import com.oc.hawk.project.domain.model.user.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ProjectFactory {
    private final UserFacade userFacade;

    public Project create(RegisterProjectCommand command) {
        BuildType buildType = BuildType.valueOf(command.getBuildType());
        BuildCommand buildCommand = BuildCommand.createOrDefault(command.getBuildCommand(), buildType.getCommand());
        ProjectBuild projectBuild = new ProjectBuild(buildType, buildCommand, command.getOutput());
        return Project.builder()
            .groupName(userFacade.currentUserDepartment().getGroupName())
            .name(new ProjectName(command.getName()))
            .descn(command.getDescn())
            .createdTime(LocalDateTime.now())
            .departmentId(getProjectDeptId(command))
            .runtime(ProjectRuntime.valueOf(command.getProjectType()))
            .build(projectBuild).build();
    }

    private Long getProjectDeptId(RegisterProjectCommand command) {
        UserInfo userInfo = userFacade.currentUser();
        if (userInfo.isMaster()) {
            return command.getDepartmentId();
        } else {
            return userInfo.getDepartmentId();
        }
    }
}
