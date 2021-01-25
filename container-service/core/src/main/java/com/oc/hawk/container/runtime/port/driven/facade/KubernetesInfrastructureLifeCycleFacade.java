package com.oc.hawk.container.runtime.port.driven.facade;

import com.google.common.collect.Maps;
import com.oc.hawk.api.constant.KafkaTopic;
import com.oc.hawk.container.api.command.CreateInstanceVolumeSpecCommand;
import com.oc.hawk.container.api.command.CreateRuntimeInfoSpecCommand;
import com.oc.hawk.container.api.command.CreateServiceEntryPointCommand;
import com.oc.hawk.container.domain.config.ContainerConfiguration;
import com.oc.hawk.container.domain.facade.InfrastructureLifeCycleFacade;
import com.oc.hawk.container.domain.model.app.ServiceApp;
import com.oc.hawk.container.domain.model.app.ServiceAppRule;
import com.oc.hawk.container.domain.model.app.ServiceAppVersion;
import com.oc.hawk.container.domain.model.project.ProjectRuntimeConfig;
import com.oc.hawk.container.domain.model.runtime.config.*;
import com.oc.hawk.container.domain.model.runtime.config.volume.AppInstanceVolume;
import com.oc.hawk.container.domain.model.runtime.config.volume.InstanceVolume;
import com.oc.hawk.container.domain.model.runtime.config.volume.LogInstanceVolume;
import com.oc.hawk.container.domain.model.runtime.config.volume.NormalInstanceVolume;
import com.oc.hawk.container.domain.service.InstanceConfigDecoratorFacade;
import com.oc.hawk.container.domain.service.RuntimeInstanceConfigDecoratorService;
import com.oc.hawk.container.runtime.port.driven.facade.feign.KubernetesGateway;
import com.oc.hawk.container.api.event.RuntimeDomainEventType;
import com.oc.hawk.ddd.event.DomainEvent;
import com.oc.hawk.ddd.event.EventPublisher;
import com.oc.hawk.kubernetes.api.constants.RuntimeInfoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class KubernetesInfrastructureLifeCycleFacade implements InfrastructureLifeCycleFacade {
    public static final String BUSINESS_RUNTIME_CATALOG = "BUSINESS";
    private final KubernetesGateway kubernetesGateway;
    private final EventPublisher eventPublisher;

    private final ContainerConfiguration containerConfiguration;
    private InstanceConfigDecoratorFacade instanceConfigDecoratorFacade;

    @PostConstruct
    public void startup() {
        instanceConfigDecoratorFacade = new RuntimeInstanceConfigDecoratorService(containerConfiguration);
    }


    @Override
    public void start(InstanceConfig config, ProjectRuntimeConfig runtimeConfig) {
        instanceConfigDecoratorFacade.assemble(config);

        CreateRuntimeInfoSpecCommand spec = new CreateRuntimeInfoSpecCommand();

        BaseInstanceConfig baseConfig = (BaseInstanceConfig) config.getBaseConfig();
        spec.setInstanceId(baseConfig.getId().getId());

        InstanceHost host = baseConfig.getHost();
        InstanceNetwork network = baseConfig.getNetwork();
        InstanceImage image = baseConfig.getImage();

        spec.setEnv(host.getEnv());
        spec.setPerformance(baseConfig.getPerformanceLevel().name());
        spec.setHealthCheckPath(baseConfig.getHealthCheckPath());
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

        Set<InstanceVolume> volumes = baseConfig.getVolumes();
        for (InstanceVolume volume : volumes) {
            if (volume instanceof NormalInstanceVolume) {
                spec.getVolume().add(new CreateInstanceVolumeSpecCommand(volume.getMountPath(), volume.getVolumeName(), false));
            } else if (volume instanceof AppInstanceVolume) {
                spec.setAppVolume(new CreateInstanceVolumeSpecCommand(volume.getMountPath(), volume.getVolumeName(), true));
            } else if (volume instanceof LogInstanceVolume) {
                spec.getVolume().add(new CreateInstanceVolumeSpecCommand(volume.getMountPath(), volume.getVolumeName(), false, ((LogInstanceVolume) volume).getSubPath()));
            }
        }

        spec.setProjectId(baseConfig.getProjectId());
        spec.setLabels(new InstanceLabelConfig(config).withRequiredLabels().labels());
        spec.setRuntimeCatalog(BUSINESS_RUNTIME_CATALOG);
        spec.setEntryPoint(createEntryPoint(network));
        if (runtimeConfig != null) {
            spec.setAppImage(runtimeConfig.getImage().getAppImage());
        }
        eventPublisher.publishEvent(KafkaTopic.INFRASTRUCTURE_RESOURCE_TOPIC, DomainEvent.byData(baseConfig.getId().getId(), RuntimeDomainEventType.RUNTIME_START_EVENT, spec));

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

    private CreateServiceEntryPointCommand createEntryPoint(InstanceNetwork network) {
        CreateServiceEntryPointCommand createServiceEntryPointCommand = new CreateServiceEntryPointCommand();
        createServiceEntryPointCommand.setExtraPorts(network.getAllExposePorts());
        createServiceEntryPointCommand.setInnerPort(network.getInnerPort());
        createServiceEntryPointCommand.setServiceName(network.getServiceName());
        return createServiceEntryPointCommand;
    }
}
