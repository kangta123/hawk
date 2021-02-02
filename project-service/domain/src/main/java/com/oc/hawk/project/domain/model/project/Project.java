package com.oc.hawk.project.domain.model.project;


import com.oc.hawk.ddd.AggregateRoot;
import com.oc.hawk.project.domain.model.codebase.CodeBaseID;
import com.oc.hawk.project.domain.model.projectapp.ProjectApp;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Getter
@Builder
@AggregateRoot
public class Project {
    public static final String ROOT_PATH = "./";
    private final ProjectID projectId;
    private final ProjectName name;
    private final String descn;
    private final String groupName;

    private final ProjectBuild build;

    private final ProjectRuntime runtime;
    private final Long departmentId;
    private final LocalDateTime createdTime;
    private final ProjectBuildMode mode;
    private CodeBaseID codeBaseId;
    private Set<ProjectApp> apps;

    public String buildType() {
        return String.valueOf(build.getType());
    }

    public String buildCommand() {
        return this.build.getCommand().getCommand();
    }

    public void assignCodeBase(CodeBaseID codeBaseId) {
        this.codeBaseId = codeBaseId;
    }

    public void addProjectApp(String branch, String appName, String appPath) {
        if (apps == null) {
            apps = new HashSet<>();
        }
        if (!StringUtils.equals(appPath, ROOT_PATH)) {
            this.apps.add(new ProjectApp(branch, appName, appPath));
        }
    }
}
