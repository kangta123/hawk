package com.oc.hawk.project.port.driven.persistence.po;

import com.oc.hawk.common.hibernate.BaseEntity;
import com.oc.hawk.project.domain.model.projectapp.ProjectApp;
import com.oc.hawk.project.domain.model.projectapp.ProjectAppID;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Table(name = "project_app")
@Entity
public class ProjectAppPO extends BaseEntity {

    private Long projectId;
    private String branch;

    private String appName;
    private String appPath;


    public static ProjectAppPO create(ProjectApp app) {
        ProjectAppPO projectAppPo = new ProjectAppPO();
        if (app.getId() != null) {
            projectAppPo.setId(app.getId().getId());
        }
        projectAppPo.setAppName(app.getAppName());
        projectAppPo.setAppPath(app.getAppPath());
        projectAppPo.setBranch(app.getBranch());
        return projectAppPo;
    }

    public ProjectApp toProjectApp() {
        return new ProjectApp(new ProjectAppID(getId()), branch, appName, appPath);
    }
}
