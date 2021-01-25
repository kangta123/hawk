package com.oc.hawk.container.api.event;

public interface RuntimeDomainEventType {
    String RUNTIME_START_EVENT = "RuntimeStartEvent";
    String RUNTIME_BUILD_STOP_EVENT = "RuntimeBuildStopEvent";
    String RUNTIME_STOP_EVENT = "RuntimeStopEvent";
    String RUNTIME_DELETE_EVENT = "RuntimeDeleteEvent";
    String RUNTIME_WATCH_LOG_EVENT = "RuntimeNormalExecuteCommandEvent";

    String RUNTIME_STATE_UPDATED_EVENT = "RuntimeUpdateEvent";
    String RUNTIME_STARTED = "RuntimeStarted";
    String RUNTIME_START_FAILED = "RuntimeStartFailed";
    String RUNTIME_ENTRYPOINT_UPDATED = "RuntimeEntrypointUpdated";

    String INSTANCE_CONFIG_DELETED = "InstanceConfigDeleted";
    String INSTANCE_CONFIG_CREATED = "InstanceConfigCreated";
    String INSTANCE_CONFIG_UPDATED = "InstanceConfigUpdated";
    String INSTANCE_BUILD_AUTH_DEPLOY= "InstanceBuildAuthDeploy";

}
