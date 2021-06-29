package com.oc.hawk.container.runtime.application.representation;

import com.google.common.collect.Maps;
import com.oc.hawk.container.api.command.CreateInstanceVolumeSpecCommand;
import com.oc.hawk.container.api.command.CreateRuntimeInfoSpecCommand;
import com.oc.hawk.container.api.command.CreateServiceEntryPointCommand;
import com.oc.hawk.container.domain.config.ContainerConfiguration;
import com.oc.hawk.container.domain.config.ContainerRuntimeConfig;
import com.oc.hawk.container.domain.facade.ProjectFacade;
import com.oc.hawk.container.domain.model.runtime.build.ProjectBuildEnv;
import com.oc.hawk.container.domain.model.runtime.build.ProjectBuildLabel;
import com.oc.hawk.container.domain.model.runtime.build.ProjectType;
import com.oc.hawk.container.domain.model.runtime.config.*;
import com.oc.hawk.container.domain.model.runtime.config.volume.AppInstanceVolume;
import com.oc.hawk.container.domain.model.runtime.config.volume.HostedInstanceVolume;
import com.oc.hawk.container.domain.model.runtime.config.volume.InstanceVolume;
import com.oc.hawk.container.domain.model.runtime.config.volume.SharedInstanceVolume;
import com.oc.hawk.project.api.dto.ProjectBuildReadyDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author kangta123
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class InstanceRuntimeRepresentation {
    public static final String RUNTIME_CATALOG_BUILD = "BUILD";
    public static final String RUNTIME_CATALOG_BUSINESS = "BUSINESS";
    private final ContainerConfiguration containerConfiguration;
    private final ContainerRuntimeConfig containerRuntimeConfig;
    private final ProjectFacade projectFacade;

    public CreateRuntimeInfoSpecCommand buildRuntimeSpecCommand(Long buildJobId, ProjectBuildReadyDTO data) {
        final ContainerRuntimeConfig.ContainerConfig config = containerRuntimeConfig.getConfig(data.getRuntimeType(), data.getBuildType());

        final Map<String, Object> env = data.getEnv();
        env.putAll(ProjectBuildEnv.map(config, containerConfiguration));

        CreateRuntimeInfoSpecCommand spec = new CreateRuntimeInfoSpecCommand();
        spec.setProjectId(data.getProjectId());
        spec.setEnv(Maps.transformValues(env, String::valueOf));
        spec.setRuntimeCatalog(RUNTIME_CATALOG_BUILD);
        spec.setMesh(false);
        spec.setName(getBuildRuntimeName(buildJobId));
        spec.setAppImage(config.getBuildImage());
        spec.setNamespace(containerConfiguration.getBuildNamespace());

        spec.setLabels(ProjectBuildLabel.buildLabels(buildJobId));
        spec.setWatchLog(true);

        configVolume(spec, config.getBuildVolumes());

        return spec;
    }

    private String getBuildRuntimeName(Long buildJobId) {
        return "builder-" + buildJobId;
    }

    private void configVolume(CreateRuntimeInfoSpecCommand spec, Collection<InstanceVolume> volumes) {
        for (InstanceVolume volume : volumes) {
            final List<CreateInstanceVolumeSpecCommand> createInstanceVolumeSpecCommands = spec.getVolume();
            if (volume instanceof AppInstanceVolume) {
                spec.setAppVolume(new CreateInstanceVolumeSpecCommand(volume.getMountPath(), volume.getVolumeName(), true));
            } else {
                boolean host = false;
                String subPath = null;
                if (volume instanceof HostedInstanceVolume) {
                    host = true;
                }

                if (volume instanceof SharedInstanceVolume) {
                    subPath = ((SharedInstanceVolume) volume).getSubPath();
                }

                createInstanceVolumeSpecCommands.add(new CreateInstanceVolumeSpecCommand(volume.getMountPath(), volume.getVolumeName(), host, subPath, volume.getType()));
                spec.setVolume(createInstanceVolumeSpecCommands);
            }
        }

    }

    public CreateRuntimeInfoSpecCommand runtimeInfoSpecCommand(InstanceConfig config) {
        CreateRuntimeInfoSpecCommand spec = new CreateRuntimeInfoSpecCommand();

        BaseInstanceConfig baseConfig = (BaseInstanceConfig) config.getBaseConfig();
        spec.setInstanceId(baseConfig.getId().getId());

        InstanceHost host = baseConfig.getHost();
        InstanceNetwork network = baseConfig.getNetwork();
        InstanceImage image = baseConfig.getImage();

        spec.setEnv(host.getEnv());
        spec.setPerformance(baseConfig.getPerformanceLevel().name());
        final InstanceHealthCheck healthCheck = baseConfig.getHealthCheck();
        spec.setHealthCheckPath(healthCheck.getPath());
        spec.setHealthCheckEnabled(healthCheck.isEnabled());
        spec.setDataImage(image.getFullImage(containerConfiguration));
        spec.setMesh(network.isMesh());
        InstanceName name = baseConfig.getName();
        spec.setName(name.getName());

        String namespace = baseConfig.getNamespace();
        if (StringUtils.isEmpty(namespace)) {
            namespace = containerConfiguration.getDefaultInstanceNamespace();
        }
        spec.setNamespace(namespace);

        spec.setPreStart(host.getPreStart());

        configVolume(spec, baseConfig.getVolumes());

        spec.setProjectId(baseConfig.getProjectId());
        spec.setLabels(new InstanceLabelConfig(config).withRequiredLabels().labels());
        spec.setRuntimeCatalog(RUNTIME_CATALOG_BUSINESS);
        spec.setEntryPoint(createEntryPoint(network));

        final ProjectType projectType = projectFacade.getProjectType(baseConfig.getProjectId());

        if (projectType != null) {
            final ContainerRuntimeConfig.ContainerConfig containerConfig = containerRuntimeConfig.getConfig(projectType.getRuntimeType(), projectType.getBuildType());
            if (containerConfig != null) {
                spec.setAppImage(containerConfig.getAppImage());
            }
        }
        return spec;
    }

    private CreateServiceEntryPointCommand createEntryPoint(InstanceNetwork network) {
        CreateServiceEntryPointCommand createServiceEntryPointCommand = new CreateServiceEntryPointCommand();
        createServiceEntryPointCommand.setExtraPorts(network.getAllExposePorts());
        createServiceEntryPointCommand.setInnerPort(network.getInnerPort());
        createServiceEntryPointCommand.setServiceName(network.getServiceName());
        return createServiceEntryPointCommand;
    }
}
