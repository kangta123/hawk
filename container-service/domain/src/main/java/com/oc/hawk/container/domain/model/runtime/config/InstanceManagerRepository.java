package com.oc.hawk.container.domain.model.runtime.config;

import java.util.List;

public interface InstanceManagerRepository {
    void update(long instanceId, List<InstanceManager> instanceMangers);

//    void deleteByInstance(long instanceId);
}
