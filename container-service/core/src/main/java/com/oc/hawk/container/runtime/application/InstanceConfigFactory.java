package com.oc.hawk.container.runtime.application;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.oc.hawk.common.utils.MapUtils;
import com.oc.hawk.container.api.command.ChangeInstanceConfigCommand;
import com.oc.hawk.container.api.command.CreateInstanceConfigCommand;
import com.oc.hawk.container.api.dto.InstanceVolumeDTO;
import com.oc.hawk.container.domain.facade.ProjectFacade;
import com.oc.hawk.container.domain.facade.UserFacade;
import com.oc.hawk.container.domain.model.runtime.UserInfo;
import com.oc.hawk.container.domain.model.runtime.build.ProjectType;
import com.oc.hawk.container.domain.model.runtime.config.*;
import com.oc.hawk.container.domain.model.runtime.config.exception.InstanceConfigCreateIllegalArgumentException;
import com.oc.hawk.container.domain.model.runtime.config.volume.InstanceVolume;
import com.oc.hawk.container.domain.model.runtime.config.volume.NormalInstanceVolume;
import com.oc.hawk.container.domain.model.runtime.info.PerformanceLevel;
import com.oc.hawk.container.domain.service.InstanceCreateChecker;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class InstanceConfigFactory {
    private final ProjectFacade projectFacade;
    private final UserFacade userFacade;
    private final InstanceConfigRepository instanceConfigRepository;


    public InstanceConfig updateInstanceConfig(InstanceId instanceId, ChangeInstanceConfigCommand command) {
        InstanceConfig config = instanceConfigRepository.byId(instanceId);
        BaseInstanceConfig baseConfig = (BaseInstanceConfig) config.getBaseConfig();

        baseConfig.update(command.getDescn(), command.getPerformanceLevel(), command.getHealthCheckPath());
        baseConfig.updateNetwork(command.getMesh(), command.getExposePorts());
        baseConfig.updateInstanceLog(command.getLogPath());
        baseConfig.updateInstanceManagers(getInstanceManager(command));
        baseConfig.updateImage(getBuildJob(baseConfig.getProjectId(), command));

        InstanceVolumeDTO volume = command.getVolume();
        if (volume != null) {
            baseConfig.updateInstanceVolume(volume.getVolumeName(), volume.getMountPath());
        }

        Map<String, String> env = MapUtils.toMap(command.getEnvKey(), command.getEnvValue());
        baseConfig.createOrUpdateInstanceHost(command.getHosts(), command.getPreStart(), command.getSsh(), env);

        if (config instanceof JavaInstanceConfig) {
            Map<String, String> property = MapUtils.toMap(command.getPropertyKey(), command.getPropertyValue());
            if (config instanceof SpringBootInstanceConfig) {
                ((SpringBootInstanceConfig) config).update(command.getDebug(), command.getProfile(), command.getJprofiler(), property);
            } else {
                ((JavaInstanceConfig) config).update(command.getDebug(), command.getJprofiler(), property);
            }

        }
        if (config instanceof NginxInstanceConfig) {
            NginxInstanceConfig nginxInstanceConfig = (NginxInstanceConfig) config;

            nginxInstanceConfig.update(command.getNginxLocation());
        }
        return config;
    }

    public InstanceConfig createInstanceConfig(CreateInstanceConfigCommand command, String namespace) {

        InstanceName name = new InstanceName(command.getName());

        if (new InstanceCreateChecker(instanceConfigRepository).isDuplicate(namespace, name, null)) {
            throw new InstanceConfigCreateIllegalArgumentException("实例名已经存在, " + name);
        }

        InstanceImage image = getBuildJob(command.getProjectId(), command);
        InstanceNetwork entryPoint = getInstanceEntryPoint(command);

        InstanceLog log = new InstanceLog(command.getLogPath());

        BaseInstanceConfig config = BaseInstanceConfig.builder()
            .image(image)
            .namespace(namespace)
            .descn(command.getDescn())
            .network(entryPoint)
            .healthCheckPath(command.getHealthCheckPath())
            .log(log)
            .managers(getInstanceManager(command))
            .name(name)
            .performanceLevel(PerformanceLevel.valueOf(command.getPerformanceLevel()))
            .projectId(command.getProjectId())
            .createdTime(LocalDateTime.now())
            .updatedTime(LocalDateTime.now())
            .volumes(getVolumes(command))
            .build();

        Map<String, String> env = MapUtils.toMap(command.getEnvKey(), command.getEnvValue());
        config.createOrUpdateInstanceHost(command.getHosts(), command.getPreStart(), command.getSsh(), env);
        return createConcretedProjectConfig(command, config);
    }

    private InstanceImage getBuildJob(Long projectId, ChangeInstanceConfigCommand command) {
        InstanceImageVersion version = projectFacade.getProjectBuildImage(projectId, command.getTag());
        if (version != null) {
            return version.getInstanceImage(command.getApp());
        }
        return null;
    }

    private InstanceNetwork getInstanceEntryPoint(CreateInstanceConfigCommand command) {
        Boolean mesh = command.getMesh();
        if (mesh == null) {
            mesh = true;
        }
        String serviceName = command.getServiceName();
        return InstanceNetwork.newInstanceEntryPoint(StringUtils.isEmpty(serviceName) ? command.getName() : serviceName, mesh, command.getInnerPort(), command.getExposePorts());
    }


    private InstanceConfig createConcretedProjectConfig(CreateInstanceConfigCommand command, BaseInstanceConfig config) {
        ProjectType project = projectFacade.getProjectType(config.getProjectId());
        Map<String, String> env = MapUtils.toMap(command.getPropertyKey(), command.getPropertyValue());
        if (project.isSpringBoot()) {
            return new SpringBootInstanceConfig(config, env, command.getDebug(), command.getJprofiler(), command.getProfile());
        } else if (project.isTomcat()) {
            return new TomcatInstanceConfig(config, env, command.getDebug(), command.getJprofiler());
        } else if (project.isNginx()) {
            return new NginxInstanceConfig(config, command.getNginxLocation());
        } else {
            throw new IllegalArgumentException("不支持的项目类型");
        }
    }

    private List<InstanceManager> getInstanceManager(ChangeInstanceConfigCommand command) {
        List<Long> managerIds = command.getManagerIds();
        if (managerIds == null) {
            return null;
        }
        if (managerIds.isEmpty()) {
            return Lists.newArrayList();
        }
        List<UserInfo> users = userFacade.getUserInfo(managerIds);
        return InstanceManager.create(users);
    }


    private Set<InstanceVolume> getVolumes(ChangeInstanceConfigCommand command) {
        InstanceVolumeDTO commandVolume = command.getVolume();
        if (commandVolume != null) {
            return Sets.newHashSet(new NormalInstanceVolume(commandVolume.getMountPath(), commandVolume.getVolumeName()));
        }
        return null;
    }
}
