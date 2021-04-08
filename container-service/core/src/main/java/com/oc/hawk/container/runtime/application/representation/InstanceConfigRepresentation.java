package com.oc.hawk.container.runtime.application.representation;

import com.oc.hawk.container.api.dto.*;
import com.oc.hawk.container.domain.config.ContainerConfiguration;
import com.oc.hawk.container.domain.model.runtime.config.*;
import com.oc.hawk.container.domain.model.runtime.config.volume.InstanceVolume;
import com.oc.hawk.kubernetes.api.constants.RuntimeInfoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InstanceConfigRepresentation {
    private final RuntimeInfoFacade infrastructureLifeCycleFacade;
    private final ContainerConfiguration containerConfiguration;

    public InstanceConfigDTO instanceConfigDtoWithRuntimeInfo(InstanceConfig instanceConfig) {
        BaseInstanceConfig baseConfig = (BaseInstanceConfig) instanceConfig.getBaseConfig();
        InstanceConfigDTO instanceConfigDTO = instanceConfigDTO(instanceConfig);
        List<RuntimeInfoDTO> infoList = infrastructureLifeCycleFacade.queryRuntimeInfo(baseConfig.getNamespace(), baseConfig.getName());
        configRuntimeInfo(instanceConfigDTO, infoList);
        return instanceConfigDTO;
    }

    public void configRuntimeInfo(InstanceConfigDTO instanceConfigDTO, List<RuntimeInfoDTO> infoList) {
        if (infoList == null || infoList.size() == 0) {
            return;
        }
        instanceConfigDTO.setContainerInfo(infoList.iterator().next());
    }

    public List<InstanceConfigDTO> instanceConfigDtoWithRuntimeInfo(List<InstanceConfig> instanceConfigs) {

        if (CollectionUtils.isEmpty(instanceConfigs)) {
            return null;
        }

        Map<RuntimeInfoEntryKey, List<RuntimeInfoDTO>> runtimeInfo = getRuntimeInfo(instanceConfigs);

        return instanceConfigs.stream()
            .map(this::instanceConfigDTO)
            .peek(config -> {
                List<RuntimeInfoDTO> infoList = runtimeInfo.getOrDefault(RuntimeInfoEntryKey.create(config), null);
                configRuntimeInfo(config, infoList);
            }).collect(Collectors.toList());

    }

    public InstanceConfigDTO instanceConfigDTO(InstanceConfig instanceConfig) {
        if (instanceConfig == null) {
            return null;
        }
        BaseInstanceConfig baseConfig = (BaseInstanceConfig) instanceConfig.getBaseConfig();
        InstanceConfigDTO dto = new InstanceConfigDTO();
        dto.setCreatedTime(baseConfig.getCreatedTime());
        dto.setUpdatedTime(baseConfig.getUpdatedTime());
        InstanceImage image = baseConfig.getImage();
        dto.setApp(image.getApp());
        InstanceHost host = baseConfig.getHost();
        if (host != null) {
            dto.setEnv(host.getEnv());
            dto.setPreStart(host.getPreStart());
            dto.setHosts(host.getHosts());
            InstanceRemoteAccess remoteAccess = host.getRemoteAccess();
            dto.setSsh(remoteAccess != null);
            if (remoteAccess != null) {
                dto.setSshPassword(remoteAccess.getSshPassword());
            }
        }
        InstanceNetwork network = baseConfig.getNetwork();
        InstanceLog log = baseConfig.getLog();
        InstanceName name = baseConfig.getName();

        dto.setDomainHost(baseConfig.getDomainHost(containerConfiguration));
        dto.setBranch(image.getBranch());
        dto.setNamespace(baseConfig.getNamespace());
        dto.setDescn(baseConfig.getDescn());
        dto.setInnerPort(network.getInnerPort());
        dto.setExposePorts(network.getExposePorts());
        dto.setCustomExposePorts(network.getCustomExposePorts());

        final InstanceHealthCheck healthCheck = baseConfig.getHealthCheck();
        dto.setHealthCheckPath(healthCheck.getPath());
        dto.setHealthCheck(healthCheck.isEnabled());

        if (baseConfig.getId() != null) {
            dto.setId(baseConfig.getId().getId());
        }

        dto.setApp(image.getApp());
        dto.setLogPath(log.getLogPath());
        dto.setMesh(network.isMesh());
        dto.setName(name.getName());
        dto.setPerformanceLevel(baseConfig.getPerformanceLevel().name());
        dto.setProjectId(baseConfig.getProjectId());
        dto.setServiceName(network.getServiceName());
        dto.setTag(image.getTag());
        dto.setVolume(instanceVolume(baseConfig));


        dto.setManagers(instanceManager(baseConfig));

        if (instanceConfig instanceof JavaInstanceConfig) {
            JavaInstanceConfig javaInstanceConfig = (JavaInstanceConfig) instanceConfig;
            if (instanceConfig instanceof SpringBootInstanceConfig) {
                dto.setProfile(((SpringBootInstanceConfig) javaInstanceConfig).getProfile());
            }
            dto.setDebug(javaInstanceConfig.getDebug());
            dto.setJprofiler(javaInstanceConfig.getJprofiler());
            dto.setProperty(javaInstanceConfig.getProperty());
        }

        if (instanceConfig instanceof NginxInstanceConfig) {
            NginxInstanceConfig nginxInstanceConfig = (NginxInstanceConfig) instanceConfig;
            dto.setNginxLocation(nginxInstanceConfig.getNginxLocation());
        }
        return dto;
    }

    public InstanceDeploymentDTO autoDeployInstance(InstanceConfig instance, Long projectBuildId) {
        final InstanceDeploymentDTO instanceDeploymentDTO = new InstanceDeploymentDTO();
        instanceDeploymentDTO.setInstance(this.instanceConfigDTO(instance));
        instanceDeploymentDTO.setProjectBuildJobId(projectBuildId);
        return instanceDeploymentDTO;
    }


    private Map<RuntimeInfoEntryKey, List<RuntimeInfoDTO>> getRuntimeInfo(List<InstanceConfig> instanceConfigs) {
        BaseInstanceConfig baseConfig = (BaseInstanceConfig) instanceConfigs.iterator().next().getBaseConfig();
        long projectId = baseConfig.getProjectId();
        String namespace = baseConfig.getNamespace();
        List<RuntimeInfoDTO> containerInfoList = infrastructureLifeCycleFacade.queryRuntimeInfo(namespace, projectId);
        return containerInfoList.stream().collect(Collectors.groupingBy(RuntimeInfoEntryKey::create));
    }

    private List<InstanceManagerDTO> instanceManager(BaseInstanceConfig baseConfig) {
        List<InstanceManager> managers = baseConfig.getManagers();
        if (managers != null) {
            return managers.stream().map(m -> {
                InstanceManagerDTO instanceManagerDTO = new InstanceManagerDTO();
                instanceManagerDTO.setUserId(m.getUserId());
                instanceManagerDTO.setUsername(m.getUsername());
                return instanceManagerDTO;
            }).collect(Collectors.toList());
        }
        return null;
    }

    private InstanceVolumeDTO instanceVolume(BaseInstanceConfig baseConfig) {
        Set<InstanceVolume> volumes = baseConfig.getVolumes();

        if (volumes != null && volumes.size() > 0) {
            InstanceVolume volume = volumes.iterator().next();
            if (volume != null) {
                InstanceVolumeDTO instanceVolumeDTO = new InstanceVolumeDTO();
                instanceVolumeDTO.setVolumeName(volume.getVolumeName());
                instanceVolumeDTO.setMountPath(volume.getMountPath());
                return instanceVolumeDTO;
            }
        }
        return null;
    }

    private static final Boolean ENABLED = true;

    public List<InstanceProjectDTO> instanceProjectDTO(List<InstanceConfig> instanceConfigList) {
        return instanceConfigList.stream().map(m -> {
            InstanceProjectDTO dto = new InstanceProjectDTO();
            BaseInstanceConfig config = (BaseInstanceConfig) m.getBaseConfig();
            dto.setId(config.getId().getId());
            dto.setApp(config.getImage().getApp());
            dto.setInstanceName(config.getName().getName());
            dto.setEnabled(ENABLED);
            return dto;
        }).collect(Collectors.toList());
    }
}
