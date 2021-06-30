package com.oc.hawk.container.runtime.port.driven.facade;

import com.google.common.collect.Maps;
import com.oc.hawk.container.api.command.CreateRuntimeInfoSpecCommand;
import com.oc.hawk.container.api.command.StopRuntimeInfoCommand;
import com.oc.hawk.container.api.event.ContainerDomainEventType;
import com.oc.hawk.container.domain.config.ContainerConfiguration;
import com.oc.hawk.container.domain.facade.InfrastructureLifeCycleFacade;
import com.oc.hawk.container.domain.model.app.ServiceApp;
import com.oc.hawk.container.domain.model.app.ServiceAppRule;
import com.oc.hawk.container.domain.model.app.ServiceAppVersion;
import com.oc.hawk.container.domain.model.runtime.config.BaseInstanceConfig;
import com.oc.hawk.container.domain.model.runtime.config.InstanceConfig;
import com.oc.hawk.container.domain.service.InstanceConfigDecoratorFacade;
import com.oc.hawk.container.domain.service.RuntimeInstanceConfigDecoratorService;
import com.oc.hawk.container.runtime.application.representation.InstanceRuntimeRepresentation;
import com.oc.hawk.container.runtime.port.driven.facade.feign.KubernetesGateway;
import com.oc.hawk.ddd.event.DomainEvent;
import com.oc.hawk.ddd.event.EventPublisher;
import com.oc.hawk.kubernetes.api.constants.RuntimeInfoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class KubernetesInfrastructureLifeCycleFacade implements InfrastructureLifeCycleFacade {
    private final KubernetesGateway kubernetesGateway;
    private final EventPublisher eventPublisher;
    private final ContainerConfiguration containerConfiguration;
    private final InstanceRuntimeRepresentation instanceRuntimeRepresentation;
    private InstanceConfigDecoratorFacade instanceConfigDecoratorFacade;

    @PostConstruct
    public void startup() {
        instanceConfigDecoratorFacade = new RuntimeInstanceConfigDecoratorService(containerConfiguration);
    }


    @Override
    public void start(InstanceConfig config) {
        instanceConfigDecoratorFacade.decorate(config);
        final CreateRuntimeInfoSpecCommand createRuntimeInfoSpecCommand = instanceRuntimeRepresentation.runtimeInfoSpecCommand(config);
        eventPublisher.publishDomainEvent(DomainEvent.byData(config.getId().getId(), ContainerDomainEventType.INSTANCE_STARTED, createRuntimeInfoSpecCommand));
    }

    @Override
    public void stop(InstanceConfig config) {
        BaseInstanceConfig baseConfig = (BaseInstanceConfig) config.getBaseConfig();

        final long id = baseConfig.getId().getId();
        StopRuntimeInfoCommand command = new StopRuntimeInfoCommand(id, baseConfig.getNamespace(), baseConfig.getName().getName(), baseConfig.getProjectId());

        eventPublisher.publishDomainEvent(DomainEvent.byData(id, ContainerDomainEventType.INSTANCE_STOPPED, command));
    }

    @Override
    public Map<Long, Integer> countRuntimeByProject(String namespace) {
        List<RuntimeInfoDTO> runtimeInfo;
        try {
            runtimeInfo = kubernetesGateway.queryRuntimeInfo(namespace, null, null, false);
        } catch (RuntimeException e) {
            runtimeInfo = null;
            log.warn("Disconnect with kubernetes server, due to network issue...");
        }
        if (runtimeInfo == null || runtimeInfo.isEmpty()) {
            return null;
        }
        Map<Long, Long> result = runtimeInfo
            .stream()
            .map(RuntimeInfoDTO::getProjectId)
            .filter(Objects::nonNull)
            .map(Long::parseLong)
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        return Maps.transformValues(result, Math::toIntExact);

    }

    @Override
    public void scale(ServiceAppVersion version) {
        InstanceConfig config = version.getConfig();
        BaseInstanceConfig baseConfig = (BaseInstanceConfig) config.getBaseConfig();
        kubernetesGateway.scaleService(baseConfig.getName().getName(), version.getApp().getNamespace(), version.getScale());
    }

    @Override
    public void applyServiceAppRules(ServiceApp app, List<String> versionNames, List<ServiceAppRule> appRules) {
//        ServiceAppRuleConverter converter = new DefaultServiceAppRuleConvert();
//
//        HawkHttpPolicyRequestDTO hawkHttpPolicyRequestDTO = new HawkHttpPolicyRequestDTO();
//        hawkHttpPolicyRequestDTO.setServiceName(app.getName());
//        hawkHttpPolicyRequestDTO.setNamespace(app.getNamespace());
//        hawkHttpPolicyRequestDTO.setVersions(versionNames);
//
//        List<HawkHttpPolicyDTO> policy = appRules.stream().map(converter::convertHttpRule).collect(Collectors.toList());
//
//        hawkHttpPolicyRequestDTO.setPolicy(policy);
//
//
//        kubernetesGateway.applyServiceAppRules(hawkHttpPolicyRequestDTO);
    }

}
