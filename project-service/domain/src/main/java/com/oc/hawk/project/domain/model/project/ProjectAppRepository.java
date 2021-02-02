package com.oc.hawk.project.domain.model.project;

import com.oc.hawk.project.domain.model.projectapp.ProjectApp;
import com.oc.hawk.project.domain.model.projectapp.ProjectAppID;

import java.util.List;

public interface ProjectAppRepository {
    List<ProjectApp> byProjectId(long id);

    List<ProjectApp> byIds(List<ProjectAppID> appIds);
}
