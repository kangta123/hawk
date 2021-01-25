package com.oc.hawk.message.domain.facade;

import com.oc.hawk.message.domain.model.ProjectBuildCreator;

public interface ProjectBuildFacade {

    ProjectBuildCreator getBuildJobCreator(Long jobId);
}
