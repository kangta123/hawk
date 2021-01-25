package com.oc.hawk.project.api.event;

public interface ProjectDomainEventType {
    String PROJECT_BUILD_JOB_CREATED = "ProjectBuildJobCreated";
    String PROJECT_BUILD_JOB_READY = "ProjectBuildJobReady";
    String PROJECT_BUILD_UPDATE_TAG = "ProjectBuildUpdateTag";
    String PROJECT_BUILD_UPDATE_SUB_APPS = "ProjectBuildUpdateSubApps";
    String PROJECT_BUILD_JOB_END = "ProjectBuildJobEnd";

    String PROJECT_BUILD_JOB_FAILED = "ProjectBuildJobFailed";

    String PROJECT_CREATED = "ProjectCreated";
    String PROJECT_DELETED = "ProjectDeleted";
}
