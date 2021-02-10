package com.oc.hawk.kubernetes.runtime.application.runtime.spec;

import com.oc.hawk.container.domain.config.HealthCheckProperties;
import com.oc.hawk.container.domain.model.runtime.info.PerformanceLevel;
import com.oc.hawk.container.domain.model.runtime.info.RuntimeCatalog;
import com.oc.hawk.container.domain.model.runtime.info.RuntimeHealthCheck;
import com.oc.hawk.container.domain.model.runtime.info.RuntimeImage;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

@Getter
@SuperBuilder
public class RuntimeConfigSpec {
    private final Long projectId;
    private final String namespace;
    private final PerformanceLevel performanceLevel;
    private final RuntimeImage image;
    private final RuntimeImage appImage;
    private final String name;
    private final NetworkConfigSpec networkConfigSpec;
    private final Map<String, String> env;
    private final String preStart;
    private final List<ServiceVolumeSpec> volumes;
    private final Map<String, String> labels;
    private final String serviceName;
    //    private final RemoteAccessConfigurationSpec remoteAccessConfigurationSpec;
    private final RuntimeHealthCheck healthCheck;
    private final String runtimeType;
    private final RuntimeCatalog catalog;

    public String getImageName() {
        return this.getImage().getImage();
    }

    public PerformanceLevel getPerformanceLevelOrDefault() {
        if (performanceLevel != null) {
            return performanceLevel;
        } else {
            return PerformanceLevel.getWithDefaultPerformanceLevel(null);
        }
    }


    public boolean isBusiness() {
        return this.catalog == RuntimeCatalog.BUSINESS;
    }
}
