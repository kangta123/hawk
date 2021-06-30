package com.oc.hawk.container.runtime.application.instance;

import com.oc.hawk.common.spring.cloud.stream.event.KafkaEventPublish;
import com.oc.hawk.container.api.command.DeleteRuntimeInfoCommand;
import com.oc.hawk.container.api.event.ContainerDomainEventType;
import com.oc.hawk.container.domain.config.ContainerConfiguration;
import com.oc.hawk.container.domain.facade.InfrastructureLifeCycleFacade;
import com.oc.hawk.container.domain.model.runtime.config.*;
import com.oc.hawk.container.domain.service.InstanceStartExecutor;
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

    private final ContainerConfiguration configuration;

    public void startOrUpdate(InstanceId instanceId) {
        log.info("start or restart instance  with id {}", instanceId);
        new InstanceStartExecutor(infrastructureLifeCycleFacade, instanceConfigRepository).start(instanceId);
    }


    public void stopInstance(InstanceId configId) {
        log.info("stop instance with id {}", configId);
        InstanceConfig config = instanceConfigRepository.byId(configId);
        infrastructureLifeCycleFacade.stop(config);
    }

    @Transactional(rollbackFor = {Exception.class})
    public void deleteInstanceByProject(long projectId) {
        log.info("delete service from projectId  id {}", projectId);

        List<InstanceConfig> instanceConfigList = instanceConfigRepository.byProject(projectId, configuration.getDefaultInstanceNamespace());
        instanceConfigList.forEach(instance -> {
            final BaseInstanceConfig baseConfig = (BaseInstanceConfig) instance.getBaseConfig();
            final InstanceId id = baseConfig.getId();
            instanceConfigRepository.delete(id);
            final InstanceName name = baseConfig.getName();
            this.deleteInstance(name.getName(), baseConfig.getNetwork().getServiceName(), configuration.getDefaultInstanceNamespace());
        });
    }

    private void deleteInstance(String name, String serviceName, String namespace) {
        log.info("delete service with name {}", name);

        DeleteRuntimeInfoCommand command = new DeleteRuntimeInfoCommand(namespace, name, serviceName);
        eventPublish.publishDomainEvent(DomainEvent.byData(ContainerDomainEventType.INSTANCE_DELETED, command));
    }

}
