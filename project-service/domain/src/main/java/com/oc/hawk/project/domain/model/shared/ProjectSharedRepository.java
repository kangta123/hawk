package com.oc.hawk.project.domain.model.shared;

import com.oc.hawk.project.domain.model.project.Project;
import com.oc.hawk.project.domain.model.project.ProjectID;

import java.util.List;

public interface ProjectSharedRepository {
    List<Project> getProjectSharedToMe();

    void sharedToUser(ProjectID projectId, List<Long> users);

    List<Long> projectSharedUsers(ProjectID projectId);
}
