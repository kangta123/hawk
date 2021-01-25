package com.oc.hawk.project.domain.service;

import com.oc.hawk.project.domain.model.buildjob.ProjectBuildExecutionEnv;
import com.oc.hawk.project.domain.model.buildjob.ProjectBuildJob;
import com.oc.hawk.project.domain.model.codebase.CodeBase;
import com.oc.hawk.project.domain.model.project.Project;

public interface ProjectBuildEnv {
    ProjectBuildExecutionEnv generateEnvVariable(ProjectBuildJob projectBuildJob, Project project, CodeBase codebase);
}
