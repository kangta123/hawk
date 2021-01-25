package com.oc.hawk.project.domain.model.project;

import com.oc.hawk.project.domain.model.projectApp.ProjectApp;
import com.oc.hawk.project.domain.model.projectApp.ProjectAppID;

import java.util.List;

public interface ProjectAppRepository {
    List<ProjectApp> byProjectId(long id);

    List<ProjectApp> byIds(List<ProjectAppID> appIds);
}
