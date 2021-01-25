package com.oc.hawk.project.application.representation.facade;

import com.oc.hawk.project.domain.model.project.ProjectID;
import com.oc.hawk.project.port.driven.facade.ProjectRuntimeCounter;

import java.util.List;
import java.util.Map;

public interface ContainerFacade {
    Map<ProjectID, ProjectRuntimeCounter> loadRuntimeCountByProjects(List<ProjectID> projectIds);
}
