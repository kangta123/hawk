package com.oc.hawk.container.api.event;

public interface ContainerDomainEventType {
    String BUILD_RUNTIME_STOPPED = "BuildRuntimeStopped";

    String RUNTIME_STATE_UPDATED = "RuntimeUpdated";
    String RUNTIME_STARTED = "RuntimeStarted";
    String RUNTIME_START_FAILED = "RuntimeStartFailed";
    String RUNTIME_ENTRYPOINT_UPDATED = "RuntimeEntrypointUpdated";

    String INSTANCE_DELETED = "InstanceDeleted";
    String INSTANCE_STOPPED = "InstanceStopped";
    String INSTANCE_CREATED = "InstanceCreated";
    String INSTANCE_STARTED = "InstanceStarted";
    String INSTANCE_CONFIG_UPDATED = "InstanceConfigUpdated";
    String INSTANCE_BUILD_AUTH_DEPLOY= "InstanceBuildAuthDeploy";

}
