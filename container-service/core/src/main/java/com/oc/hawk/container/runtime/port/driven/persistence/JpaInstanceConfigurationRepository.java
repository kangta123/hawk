package com.oc.hawk.container.runtime.port.driven.persistence;

import com.oc.hawk.api.utils.JsonUtils;
import com.oc.hawk.container.domain.facade.ProjectFacade;
import com.oc.hawk.container.domain.model.runtime.config.*;
import com.oc.hawk.container.domain.model.runtime.config.volume.InstanceVolume;
import com.oc.hawk.container.runtime.port.driven.persistence.po.InstanceConfigPO;
import com.oc.hawk.container.runtime.port.driven.persistence.po.InstanceVolumePO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Repository
interface InstanceConfigPORepository extends CrudRepository<InstanceConfigPO, Long> {
    @Query("select projectId as projectId, count(id) as count  from  InstanceConfigPO where projectId in (:ids) and namespace=:namespace group by projectId")
    List<ProjectConfigCount> queryProjectConfigCounts(@Param("ids") Collection<Long> projectIds, @Param("namespace") String namespace);

    InstanceConfigPO findByNamespaceAndName(String namespace, String name);

    @EntityGraph(attributePaths = {"managers", "volume"})
    List<InstanceConfigPO> findByProjectIdAndNamespace(Long projectId, String defaultNamespace);

    @Query("SELECT id FROM InstanceConfigPO where namespace=?1 and name = ?2")
    Long queryByNamespaceAndName(String namespace, String name);

    @Override
    @EntityGraph(attributePaths = {"managers", "volume"})
    Optional<InstanceConfigPO> findById(Long id);

    @EntityGraph(attributePaths = {"managers", "volume"})
    InstanceConfigPO findByServiceNameAndNamespace(String serviceName, String namespace);

    List<InstanceConfigPO> findByProjectId(Long projectId);

    List<InstanceConfigPO> findByProjectIdIn(List<Long> projectIdList);
}

@Repository
interface InstanceVolumePORepository extends CrudRepository<InstanceVolumePO, Long> {
}

@Component
@Slf4j
@RequiredArgsConstructor
public class JpaInstanceConfigurationRepository implements InstanceConfigRepository {

    private final InstanceConfigPORepository instanceConfigPORepository;
    private final InstanceConfigRepositoryFactory instanceConfigRepositoryFactory;
    private final ProjectFacade projectFacade;
    private final InstanceVolumePORepository instanceVolumePORepository;
    private final InstanceManagerRepository instanceManagerRepository;


    @Override
    public void delete(InstanceId id) {
        long instanceId = id.getId();
        try {
            instanceConfigPORepository.deleteById(instanceId);
        } catch (EmptyResultDataAccessException e) {

        }
    }

    @Override
    public List<InstanceConfig> byProject(Long projectId, String namespace) {
        return instanceConfigPORepository.findByProjectIdAndNamespace(projectId, namespace)
            .stream()
            .map(instanceConfigRepositoryFactory::toInstanceConfig)
            .collect(Collectors.toList());
    }

    @Override
    public InstanceConfig byProject(String namespace, InstanceName instanceName) {
        instanceName.validate(namespace);
        InstanceConfigPO instanceConfigPo = instanceConfigPORepository.findByNamespaceAndName(namespace, instanceName.getName());
        return instanceConfigRepositoryFactory.toInstanceConfig(instanceConfigPo);
    }

    @Override
    public InstanceId save(InstanceConfig config) {
        InstanceConfigPO instanceConfigPo;
        BaseInstanceConfig baseConfig = (BaseInstanceConfig) config.getBaseConfig();
        if (baseConfig.getId() != null) {
            instanceConfigPo = instanceConfigPORepository.findById(baseConfig.getId().getId()).orElse(new InstanceConfigPO());
        } else {
            instanceConfigPo = new InstanceConfigPO();
        }

        instanceConfigPo.setCreateTime(baseConfig.getCreatedTime());
        Long projectId = baseConfig.getProjectId();

        InstanceNetwork network = baseConfig.getNetwork();
        InstanceImage image = baseConfig.getImage();

        instanceConfigPo.setBranch(image.getBranch());
        instanceConfigPo.setDescn(baseConfig.getDescn());

        InstanceHost host = baseConfig.getHost();
        if (host != null) {
            instanceConfigPo.setEnv(JsonUtils.object2Json(host.getEnv()));
            instanceConfigPo.setHosts(host.getHosts());
            instanceConfigPo.setPreStart(host.getPreStart());
            configRemoteAccess(instanceConfigPo, host);
        }
        instanceConfigPo.setInnerPort(network.getInnerPort());
        instanceConfigPo.setMesh(network.isMesh());


        Map<Integer, Integer> allExposePorts = network.getAllExposePorts();
        instanceConfigPo.setExtraPorts(JsonUtils.object2Json(allExposePorts));

        if (baseConfig.getId() != null) {
            instanceConfigPo.setId(baseConfig.getId().getId());
        }

        final InstanceHealthCheck healthCheck = baseConfig.getHealthCheck();
        instanceConfigPo.setHealthCheckPath(healthCheck.getPath());
        instanceConfigPo.setHealthCheck(healthCheck.isEnabled());

        if (image.getApp() != null) {
            instanceConfigPo.setImage(image.getApp());
        }
        if (image.getTag() != null) {
            instanceConfigPo.setTag(image.getTag());
        }

        if (baseConfig.getLog() != null) {
            instanceConfigPo.setLogPath(baseConfig.getLog().getLogPath());
        }

        InstanceName name = baseConfig.getName();
        instanceConfigPo.setName(name.getName());
        instanceConfigPo.setNamespace(baseConfig.getNamespace());

        instanceConfigPo.setPerformanceLevel(baseConfig.getPerformanceLevel().name());
        instanceConfigPo.setProjectId(projectId);
        instanceConfigPo.setServiceName(network.getServiceName());

        configInstanceVolumePo(instanceConfigPo, baseConfig);
        configJavaInstanceConfigPo(config, instanceConfigPo);
        configNginxInstanceConfigPo(config, instanceConfigPo);

        instanceConfigPORepository.save(instanceConfigPo);
        instanceManagerRepository.update(instanceConfigPo.getId(), baseConfig.getManagers());
        return new InstanceId(instanceConfigPo.getId());
    }

    public void configRemoteAccess(InstanceConfigPO instanceConfigPo, InstanceHost host) {
        InstanceRemoteAccess remoteAccess = host.getRemoteAccess();
        instanceConfigPo.setSsh(remoteAccess != null);
        if (remoteAccess != null) {
            instanceConfigPo.setSshPassword(remoteAccess.getSshPassword());
        }
    }

    @Override
    public InstanceConfig byId(InstanceId id) {
        return instanceConfigPORepository.findById(id.getId()).map(instanceConfigRepositoryFactory::toInstanceConfig).orElse(null);
    }

    @Override
    public InstanceConfig byServiceName(String serviceName, String namespace) {
        InstanceConfigPO instanceConfigPo = instanceConfigPORepository.findByServiceNameAndNamespace(serviceName, namespace);
        return instanceConfigRepositoryFactory.toInstanceConfig(instanceConfigPo);
    }

    @Override
    public InstanceId existed(String namespace, InstanceName name) {
        name.validate(namespace);
        Long id = instanceConfigPORepository.queryByNamespaceAndName(namespace, name.getName());
        if (id == null) {
            return null;
        }
        return new InstanceId(id);
    }

    @Override
    public Map<Long, Integer> countInstanceByProjects(Collection<Long> projectIds, String namespace) {
        if (CollectionUtils.isEmpty(projectIds)) {
            return null;
        }
        List<ProjectConfigCount> projectConfigCounts = instanceConfigPORepository.queryProjectConfigCounts(projectIds, namespace);
        if (projectConfigCounts != null) {
            return projectConfigCounts.stream().collect(Collectors.toMap(ProjectConfigCount::getProjectId, ProjectConfigCount::getCount));
        }
        return null;
    }

    private void configNginxInstanceConfigPo(InstanceConfig config, InstanceConfigPO instanceConfigPo) {
        if (config instanceof NginxInstanceConfig) {
            NginxInstanceConfig nginxInstanceConfig = (NginxInstanceConfig) config;
            instanceConfigPo.setNginxLocation(nginxInstanceConfig.getNginxLocation());
        }
    }

    private void configJavaInstanceConfigPo(InstanceConfig config, InstanceConfigPO instanceConfigPo) {
        if (config instanceof JavaInstanceConfig) {
            JavaInstanceConfig javaInstanceConfig = (JavaInstanceConfig) config;
            Map<String, String> property = javaInstanceConfig.getProperty();
            if (property != null && !property.isEmpty()) {
                instanceConfigPo.setProperty(JsonUtils.object2Json(property));
            }
            instanceConfigPo.setDebug(javaInstanceConfig.getDebug());
            instanceConfigPo.setJprofiler(javaInstanceConfig.getJprofiler());
            if (config instanceof SpringBootInstanceConfig) {
                instanceConfigPo.setProfile(((SpringBootInstanceConfig) javaInstanceConfig).getProfile());
            }
        }
    }

    private void configInstanceVolumePo(InstanceConfigPO instanceConfigPo, BaseInstanceConfig baseConfig) {
        Set<InstanceVolume> volumes = baseConfig.getVolumes();
        if (volumes != null) {
            if (instanceConfigPo.getVolume() != null) {
                instanceVolumePORepository.delete(instanceConfigPo.getVolume());
            }
            if (volumes.size() > 0) {
                InstanceVolume volume = volumes.iterator().next();
                InstanceVolumePO instanceVolumePo = new InstanceVolumePO();
                instanceVolumePo.setMountPath(volume.getMountPath());
                instanceVolumePo.setVolumeName(volume.getVolumeName());
                instanceConfigPo.setVolume(instanceVolumePo);
                instanceVolumePORepository.save(instanceVolumePo);
            }
        }
    }

    @Override
    public List<InstanceConfig> byProjectIds(List<Long> projectIds) {
        return instanceConfigPORepository.findByProjectIdIn(projectIds).stream()
            .map(instanceConfigRepositoryFactory::toInstanceConfig)
            .collect(Collectors.toList());
    }

}
