package com.oc.hawk.container.runtime.application.instance;

import com.oc.hawk.api.constant.KafkaTopic;
import com.oc.hawk.common.spring.cloud.stream.event.KafkaEventPublish;
import com.oc.hawk.container.api.command.DeleteRuntimeInfoCommand;
import com.oc.hawk.container.api.command.StopRuntimeInfoCommand;
import com.oc.hawk.container.api.event.RuntimeDomainEventType;
import com.oc.hawk.container.domain.facade.InfrastructureLifeCycleFacade;
import com.oc.hawk.container.domain.model.project.ProjectRuntimeConfigRepository;
import com.oc.hawk.container.domain.service.InstanceStartExecutor;
import com.oc.hawk.container.domain.model.runtime.config.*;
import com.oc.hawk.ddd.event.DomainEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class InstanceExecutorUseCase {

    private final InfrastructureLifeCycleFacade infrastructureLifeCycleFacade;

    private final InstanceConfigRepository instanceConfigRepository;

    private final KafkaEventPublish eventPublish;

    private final ProjectRuntimeConfigRepository projectRuntimeConfigRepository;

    public void startOrUpdate(InstanceId instanceId) {
        log.info("start or restart instance  with id {}", instanceId);
        new InstanceStartExecutor(infrastructureLifeCycleFacade, instanceConfigRepository, projectRuntimeConfigRepository).start(instanceId);
    }


    public void stopService(InstanceId configId) {
        log.info("stop instance with id {}", configId);

        InstanceConfig config = instanceConfigRepository.byId(configId);
        stopService(config, null);
    }

    private void stopService(InstanceConfig config, String reason) {
        BaseInstanceConfig baseConfig = (BaseInstanceConfig) config.getBaseConfig();

        final long id = baseConfig.getId().getId();
        StopRuntimeInfoCommand command = new StopRuntimeInfoCommand(id, baseConfig.getNamespace(), baseConfig.getName().getName(), baseConfig.getProjectId(), reason);

        eventPublish.publishEvent(KafkaTopic.INFRASTRUCTURE_RESOURCE_TOPIC, DomainEvent.byData(id, RuntimeDomainEventType.RUNTIME_STOP_EVENT, command));
    }


    @Transactional(rollbackFor = {Exception.class})
    public void deleteServiceByProject(long projectId) {
        log.info("delete service from projectId  id {}", projectId);

        final String namespace = InstanceDomain.DEFAULT_NAMESPACE;
        List<InstanceConfig> instanceConfigList = instanceConfigRepository.byProject(projectId, namespace);
        instanceConfigList.forEach(instance -> {
            final BaseInstanceConfig baseConfig = (BaseInstanceConfig) instance.getBaseConfig();
            final InstanceId id = baseConfig.getId();
            instanceConfigRepository.delete(id);
            final InstanceName name = baseConfig.getName();
            this.deleteService(name.getName(), baseConfig.getNetwork().getServiceName(), namespace);
        });
    }

    public void deleteService(String name, String serviceName, String namespace) {
        log.info("delete service with name {}", name);

        DeleteRuntimeInfoCommand command = new DeleteRuntimeInfoCommand(namespace, name, serviceName);
        eventPublish.publishEvent(KafkaTopic.INFRASTRUCTURE_RESOURCE_TOPIC, DomainEvent.byData(RuntimeDomainEventType.RUNTIME_DELETE_EVENT, command));
    }

}
