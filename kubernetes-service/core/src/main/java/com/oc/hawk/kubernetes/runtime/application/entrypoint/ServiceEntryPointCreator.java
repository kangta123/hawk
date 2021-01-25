package com.oc.hawk.kubernetes.runtime.application.entrypoint;

import com.oc.hawk.container.api.event.EntryPointUpdatedEvent;
import com.oc.hawk.kubernetes.runtime.application.runtime.spec.NetworkConfigSpec;

public interface ServiceEntryPointCreator {
    EntryPointUpdatedEvent createServiceEntryPoint(String namespace, Long projectId, NetworkConfigSpec networkConfigSpec);
}
