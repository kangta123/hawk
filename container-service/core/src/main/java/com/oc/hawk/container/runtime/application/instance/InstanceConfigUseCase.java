package com.oc.hawk.container.runtime.application.instance;

import com.oc.hawk.container.api.command.ChangeInstanceConfigCommand;
import com.oc.hawk.container.api.command.CreateInstanceConfigCommand;
import com.oc.hawk.container.api.dto.InstanceConfigDTO;
import com.oc.hawk.container.api.dto.InstanceDeploymentDTO;
import com.oc.hawk.container.api.dto.InstanceProjectDTO;
import com.oc.hawk.container.api.event.ContainerDomainEventType;
import com.oc.hawk.container.domain.config.ContainerConfiguration;
import com.oc.hawk.container.domain.facade.InfrastructureLifeCycleFacade;
import com.oc.hawk.container.domain.facade.ProjectFacade;
import com.oc.hawk.container.domain.model.runtime.config.*;
import com.oc.hawk.container.domain.service.InstanceStartExecutor;
import com.oc.hawk.container.domain.service.InstanceVersionUpdater;
import com.oc.hawk.container.runtime.application.InstanceConfigFactory;
import com.oc.hawk.container.runtime.application.representation.InstanceConfigRepresentation;
import com.oc.hawk.container.runtime.port.driven.facade.feign.BaseGateway;
import com.oc.hawk.ddd.event.DomainEvent;
import com.oc.hawk.ddd.event.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class InstanceConfigUseCase {
    protected final BaseGateway baseGateway;

    private final EventPublisher eventPublisher;

    private final InstanceConfigRepository instanceConfigRepository;
    private final InstanceConfigFactory instanceConfigFactory;
    private final InstanceConfigRepresentation instanceConfigRepresentation;
    private final ContainerConfiguration containerConfiguration;
    private final ProjectFacade projectFacade;
    private final InfrastructureLifeCycleFacade infrastructureLifeCycleFacade;
    
    public List<InstanceConfigDTO> queryDefaultNsInstanceConfig(Long projectId) {
        List<InstanceConfig> configurations = instanceConfigRepository.byProject(projectId, containerConfiguration.getDefaultInstanceNamespace());

        return instanceConfigRepresentation.instanceConfigDtoWithRuntimeInfo(configurations);
    }

    public InstanceConfigDTO createInstanceConfig(CreateInstanceConfigCommand createInstanceConfigCommand) {
        log.info("create instance config with name {}", createInstanceConfigCommand.getName());
        return createInstanceConfig(createInstanceConfigCommand, containerConfiguration.getDefaultInstanceNamespace());
    }

    public InstanceConfigDTO getConfiguration(Long id) {
        if (Objects.isNull(id)) {
            return null;
        }
        InstanceConfig instanceConfig = instanceConfigRepository.byId(new InstanceId(id));
        return instanceConfigRepresentation.instanceConfigDtoWithRuntimeInfo(instanceConfig);
    }

    public void deleteConfiguration(long id) {
        log.info("delete instance config with id {}", id);

        InstanceId instanceId = new InstanceId(id);
        InstanceConfig configuration = instanceConfigRepository.byId(instanceId);
        instanceConfigRepository.delete(instanceId);

        InstanceConfigDTO instanceConfigDTO = instanceConfigRepresentation.instanceConfigDTO(configuration);

        eventPublisher.publishDomainEvent(DomainEvent.byData(id, ContainerDomainEventType.INSTANCE_DELETED, instanceConfigDTO));
    }

    public InstanceConfigDTO getConfiguration(String namespace, String name) {
        InstanceName instanceName = new InstanceName(name);
        InstanceConfig config = instanceConfigRepository.byProject(namespace, instanceName);
        return instanceConfigRepresentation.instanceConfigDTO(config);
    }


    public InstanceConfigDTO updateInstanceConfig(long id, ChangeInstanceConfigCommand command) {
        log.info("update instance config with id {}", id);
        InstanceConfig config = instanceConfigFactory.updateInstanceConfig(new InstanceId(id), command);

        instanceConfigRepository.save(config);

        InstanceConfigDTO instanceConfigDTO = instanceConfigRepresentation.instanceConfigDTO(config);

        eventPublisher.publishDomainEvent(DomainEvent.byData(id, ContainerDomainEventType.INSTANCE_CONFIG_UPDATED, instanceConfigDTO));
        return instanceConfigDTO;
    }

    public void updateExposePorts(String serviceName, String namespace, Map<Integer, Integer> exposePoints) {
        log.info("update instance exposed port info with service name {}", serviceName);
        InstanceConfig config = instanceConfigRepository.byServiceName(serviceName, namespace);
        BaseInstanceConfig baseConfig = (BaseInstanceConfig) config.getBaseConfig();

        baseConfig.updateExposePort(exposePoints);
        instanceConfigRepository.save(config);
    }

    public void updateInstanceVersionAndRestart(InstanceId instanceId, Long projectBuildId) {
        final InstanceConfig instance = new InstanceVersionUpdater(projectFacade, instanceConfigRepository).update(instanceId, projectBuildId);
        new InstanceStartExecutor(infrastructureLifeCycleFacade, instanceConfigRepository).start(instance);

        final InstanceDeploymentDTO deployInstance = instanceConfigRepresentation.autoDeployInstance(instance, projectBuildId);
        eventPublisher.publishDomainEvent(DomainEvent.byData(instanceId.getId(), ContainerDomainEventType.INSTANCE_BUILD_AUTH_DEPLOY, deployInstance));
    }

    private InstanceConfigDTO createInstanceConfig(CreateInstanceConfigCommand command, String namespace) {
        InstanceConfig instanceConfig = instanceConfigFactory.createInstanceConfig(command, namespace);

        InstanceId instanceId = instanceConfigRepository.save(instanceConfig);

        InstanceConfigDTO instanceConfigDTO = instanceConfigRepresentation.instanceConfigDTO(instanceConfig);
        instanceConfigDTO.setId(instanceId.getId());
        eventPublisher.publishDomainEvent(DomainEvent.byData(instanceId.getId(), ContainerDomainEventType.INSTANCE_CREATED, instanceConfigDTO));
        return instanceConfigDTO;
    }
    
    public List<InstanceProjectDTO> listProjectInstances(List<Long> projectIds){
        List<InstanceConfig> configList = instanceConfigRepository.byProjectIds(projectIds);
        return instanceConfigRepresentation.instanceProjectDTO(configList);
    }
    
}
