package com.oc.hawk.kubernetes.runtime.application.runtime.spec.deployment.component;

import com.oc.hawk.container.domain.model.runtime.info.PerformanceLevel;
import io.fabric8.kubernetes.api.model.ResourceRequirements;

public interface PodResourceRequirement {
    ResourceRequirements getResourceRequirement(PerformanceLevel level);
}
