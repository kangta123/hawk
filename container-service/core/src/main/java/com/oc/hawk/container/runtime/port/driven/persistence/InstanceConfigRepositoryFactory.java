package com.oc.hawk.container.runtime.port.driven.persistence;

import com.google.common.collect.Sets;
import com.oc.hawk.api.utils.JsonUtils;
import com.oc.hawk.common.utils.MapUtils;
import com.oc.hawk.container.domain.facade.ProjectFacade;
import com.oc.hawk.container.domain.model.runtime.build.ProjectType;
import com.oc.hawk.container.domain.model.runtime.config.*;
import com.oc.hawk.container.domain.model.runtime.config.volume.InstanceVolume;
import com.oc.hawk.container.domain.model.runtime.config.volume.NormalInstanceVolume;
import com.oc.hawk.container.domain.model.runtime.info.PerformanceLevel;
import com.oc.hawk.container.runtime.port.driven.persistence.po.InstanceConfigPO;
import com.oc.hawk.container.runtime.port.driven.persistence.po.InstanceVolumePO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InstanceConfigRepositoryFactory {
    private final ProjectFacade projectFacade;

    public InstanceConfig toInstanceConfig(InstanceConfigPO instanceConfigPo) {
        if (instanceConfigPo == null) {
            return null;
        }
        InstanceImage image = new InstanceImage(instanceConfigPo.getImage(), instanceConfigPo.getTag(), instanceConfigPo.getBranch());

        InstanceRemoteAccess remoteAccess = getRemoteAccess(instanceConfigPo);

        Map<String, String> envMap = MapUtils.transfer(JsonUtils.json2Map(instanceConfigPo.getEnv()), String::valueOf);


        List<InstanceManager> managers = getInstanceManagers(instanceConfigPo);

        Map<Integer, Integer> extraPorts = getExposePorts(instanceConfigPo);
        String performanceLevel = instanceConfigPo.getPerformanceLevel();

        BaseInstanceConfig config = BaseInstanceConfig.builder()
            .image(image)
            .updatedTime(instanceConfigPo.getUpdateTime())
            .createdTime(instanceConfigPo.getCreateTime())
            .descn(instanceConfigPo.getDescn())
            .healthCheckPath(instanceConfigPo.getHealthCheckPath())
            .id(instanceConfigPo.getId() == null ? null : new InstanceId(instanceConfigPo.getId()))
            .log(new InstanceLog(instanceConfigPo.getLogPath()))
            .managers(managers)
            .namespace(instanceConfigPo.getNamespace())
            .name(new InstanceName(instanceConfigPo.getName()))
            .network(new InstanceNetwork(instanceConfigPo.getServiceName(), instanceConfigPo.getInnerPort(), instanceConfigPo.getMesh(), extraPorts))
            .performanceLevel(StringUtils.isNoneEmpty(performanceLevel) ? PerformanceLevel.valueOf(performanceLevel) : null)
            .projectId(instanceConfigPo.getProjectId())
            .volumes(getVolumes(instanceConfigPo))
            .build();

        config.createInstanceHost(instanceConfigPo.getHosts(), instanceConfigPo.getPreStart(), remoteAccess, envMap);
        return createConcretedInstanceConfig(instanceConfigPo, config);
    }

    public Set<InstanceVolume> getVolumes(InstanceConfigPO instanceConfigPo) {
        InstanceVolume volume = null;
        InstanceVolumePO volumePo = instanceConfigPo.getVolume();
        if (volumePo != null) {
            volume = new NormalInstanceVolume(volumePo.getVolumeName(), volumePo.getMountPath());
        }
        Set<InstanceVolume> volumes = Sets.newHashSet();
        if (volume != null) {
            volumes.add(volume);
        }
        return volumes;
    }

    private InstanceConfig createConcretedInstanceConfig(InstanceConfigPO instanceConfigPo, BaseInstanceConfig config) {
        final ProjectType projectType = projectFacade.getProjectType(config.getProjectId());

        if (projectType.isJava()) {
            String property = instanceConfigPo.getProperty();
            Map<String, String> propertyMap = MapUtils.transfer(JsonUtils.json2Map(property), Function.identity(), String::valueOf);
            if (projectType.isTomcat()) {
                return new TomcatInstanceConfig(config, propertyMap, instanceConfigPo.getDebug(), instanceConfigPo.getJprofiler());
            }

            if (projectType.isSpringBoot()) {
                return new SpringBootInstanceConfig(config, propertyMap, instanceConfigPo.getDebug(), instanceConfigPo.getJprofiler(), instanceConfigPo.getProfile());
            }
        } else if (projectType.isNginx()) {
            return new NginxInstanceConfig(config, instanceConfigPo.getNginxLocation());
        }
        return config;
    }

    private InstanceRemoteAccess getRemoteAccess(InstanceConfigPO instanceConfigPo) {
        InstanceRemoteAccess remoteAccess = null;
        if (Boolean.TRUE.equals(instanceConfigPo.getSsh())) {
            remoteAccess = new InstanceRemoteAccess(instanceConfigPo.getSshPassword());
        }
        return remoteAccess;
    }

    private List<InstanceManager> getInstanceManagers(InstanceConfigPO instanceConfigPo) {
        List<InstanceManager> managers = null;
        if (instanceConfigPo.getManagers() != null) {
            managers = instanceConfigPo.getManagers().stream().map(m -> new InstanceManager(m.getUsername(), m.getUserId())).collect(Collectors.toList());
        }
        return managers;
    }

    private Map<Integer, Integer> getExposePorts(InstanceConfigPO instanceConfigPo) {
        Map<Integer, Integer> extraPorts = null;
        if (instanceConfigPo.getExtraPorts() != null) {
            extraPorts = MapUtils.transfer(JsonUtils.json2Map(instanceConfigPo.getExtraPorts()), Integer::parseInt, v -> Integer.parseInt(String.valueOf(v)));
        }
        return extraPorts;
    }

}
