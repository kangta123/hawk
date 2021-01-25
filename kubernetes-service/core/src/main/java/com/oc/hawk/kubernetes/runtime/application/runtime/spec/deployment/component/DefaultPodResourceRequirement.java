package com.oc.hawk.kubernetes.runtime.application.runtime.spec.deployment.component;

import com.oc.hawk.container.domain.model.runtime.info.PerformanceLevel;
import io.fabric8.kubernetes.api.model.Quantity;
import io.fabric8.kubernetes.api.model.ResourceRequirements;
import io.fabric8.kubernetes.api.model.ResourceRequirementsBuilder;

public class DefaultPodResourceRequirement implements PodResourceRequirement {
    @Override
    public ResourceRequirements getResourceRequirement(PerformanceLevel performanceLevel) {
        if (performanceLevel.isUnlimited()) {
            return null;
        }
        Quantity memLimit = new Quantity(performanceLevel.getLimitMem());
        Quantity memRes = new Quantity(performanceLevel.getResMem());
        Quantity cpuLimit = new Quantity(performanceLevel.getLimitCpu());
        Quantity cpuRes = new Quantity(performanceLevel.getResCpu());

        return new ResourceRequirementsBuilder()
            .addToLimits("cpu", cpuLimit)
            .addToLimits("memory", memLimit)
            .addToRequests("cpu", cpuRes)
            .addToRequests("memory", memRes).build();
    }
}
