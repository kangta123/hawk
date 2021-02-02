package com.oc.hawk.container.domain.facade;

import com.oc.hawk.container.domain.model.runtime.build.ProjectBuild;
import com.oc.hawk.container.domain.model.runtime.build.ProjectType;
import com.oc.hawk.container.domain.model.runtime.config.InstanceImageVersion;

import java.util.List;


public interface ProjectFacade {
    Integer getProjectTotalCount();

    List<Long> getProjectIds();

    ProjectType getProjectType(Long id);

    InstanceImageVersion getProjectBuildImage(long projectId, String tag);

    ProjectBuild getProjectBuild(long buildJobId);


}
