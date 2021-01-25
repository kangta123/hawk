package com.oc.hawk.project.domain.model.projectApp;

import com.google.common.base.Objects;
import com.oc.hawk.ddd.DomainValueObject;
import lombok.Data;

@Data
@DomainValueObject
public class ProjectApp {
    private ProjectAppID id;
    private String branch;

    private String appName;
    private String appPath;

    public ProjectApp(String branch, String appName, String appPath) {
        this.branch = branch;
        this.appName = appName;
        this.appPath = appPath;
    }

    public ProjectApp(ProjectAppID id, String branch, String appName, String appPath) {
        this.id = id;
        this.branch = branch;
        this.appName = appName;
        this.appPath = appPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProjectApp that = (ProjectApp) o;
        return Objects.equal(appName, that.appName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(branch, appName, appPath);
    }
}
