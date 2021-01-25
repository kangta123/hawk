package com.oc.hawk.container.domain.model.project;

import java.util.List;

public interface ProjectRuntimeConfigRepository {
    ProjectRuntimeConfig findBy(String buildType, String runtimeType);
    List<ProjectRuntimeConfig> findBy(String runtimeType);
}
