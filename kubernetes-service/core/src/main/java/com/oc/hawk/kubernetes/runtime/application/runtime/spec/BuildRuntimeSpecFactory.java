package com.oc.hawk.kubernetes.runtime.application.runtime.spec;

import com.google.common.collect.Lists;
import com.oc.hawk.container.api.command.CreateInstanceVolumeSpecCommand;
import com.oc.hawk.container.api.command.CreateRuntimeInfoSpecCommand;
import com.oc.hawk.container.api.command.CreateServiceEntryPointCommand;
import com.oc.hawk.container.domain.config.HealthCheckProperties;
import com.oc.hawk.container.domain.model.runtime.info.PerformanceLevel;
import com.oc.hawk.container.domain.model.runtime.info.RuntimeCatalog;
import com.oc.hawk.container.domain.model.runtime.info.RuntimeHealthCheck;
import com.oc.hawk.container.domain.model.runtime.info.RuntimeImage;
import com.oc.hawk.kubernetes.runtime.application.runtime.spec.container.VolumeFileType;
import com.oc.hawk.kubernetes.runtime.application.runtime.spec.container.VolumeType;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BuildRuntimeSpecFactory {
    private final HealthCheckProperties healthCheckProperties;

    public RuntimeConfigSpec toRuntimeConfiguration(CreateRuntimeInfoSpecCommand spec) {
        if (StringUtils.isEmpty(spec.getRuntimeCatalog())) {
            throw new IllegalArgumentException("RuntimeCatalog不能为空");
        }
        RuntimeCatalog catalog = RuntimeCatalog.getCatalog(spec.getRuntimeCatalog());
        if (catalog == null) {
            throw new IllegalArgumentException("RuntimeCatalog不存在");
        }
        return buildBaseRuntimeConfiguration(catalog, spec);
    }

    private RuntimeConfigSpec buildBaseRuntimeConfiguration(RuntimeCatalog catalog, CreateRuntimeInfoSpecCommand spec) {
        NetworkConfigSpec configurationSpec = buildNetworkConfiguration(spec);
        RuntimeHealthCheck healthCheck = null;

        PerformanceLevel level;
        switch (catalog) {
            case BUILD:
                level = PerformanceLevel.getWithDefaultPerformanceLevel(spec.getPerformance());
                break;
            case BUSINESS:
                level = PerformanceLevel.getWithDefaultPerformanceLevel(null);
                if (configurationSpec != null) {
                    healthCheck = new RuntimeHealthCheck(spec.getHealthCheckPath(), spec.getHealthCheckEnabled(), configurationSpec.getInnerPort(), healthCheckProperties);
                }
                break;
            default:
                level = PerformanceLevel.UNLIMITED;
        }

        return RuntimeConfigSpec.builder()
                .env(spec.getEnv())
                .appImage(new RuntimeImage(spec.getAppImage()))
                .image(new RuntimeImage(spec.getDataImage()))
                .projectId(spec.getProjectId())
                .catalog(catalog)
                .preStart(spec.getPreStart())
                .labels(spec.getLabels())
                .performanceLevel(level)
                .name(spec.getName())
                .namespace(spec.getNamespace())
                .networkConfigSpec(configurationSpec)
                .volumes(getVolumes(spec))
                .healthCheck(healthCheck)
                .serviceName(spec.getEntryPoint() == null ? "" : spec.getEntryPoint().getServiceName())
                .build();
    }

    private List<ServiceVolumeSpec> getVolumes(CreateRuntimeInfoSpecCommand spec) {

        List<ServiceVolumeSpec> volumeSpecs = Lists.newArrayList();
        List<CreateInstanceVolumeSpecCommand> volumes = spec.getVolume();
        if (volumes != null) {
            volumeSpecs = volumes.stream().map(v ->
                    new ServiceVolumeSpec(v.getMountPath(), v.getVolumeName(), v.isHost() ? VolumeType.host : VolumeType.pvc, v.getSubPath(), VolumeFileType.withDefault(v.getType())))
                    .collect(Collectors.toList());
        }
        CreateInstanceVolumeSpecCommand appVolume = spec.getAppVolume();
        if (appVolume != null) {
            volumeSpecs.add(new ServiceVolumeSpec(appVolume.getMountPath(), appVolume.getVolumeName(), VolumeType.empty));
        }
        return volumeSpecs;
    }

    private NetworkConfigSpec buildNetworkConfiguration(CreateRuntimeInfoSpecCommand spec) {
        CreateServiceEntryPointCommand entryPoint = spec.getEntryPoint();
        if (entryPoint == null) {
            return null;
        }
        return NetworkConfigSpec.builder()
                .extraPorts(entryPoint.getExtraPorts())
                .serviceName(entryPoint.getServiceName())
                .innerPort(entryPoint.getInnerPort())
                .mesh(spec.getMesh())
                .build();
    }
}
