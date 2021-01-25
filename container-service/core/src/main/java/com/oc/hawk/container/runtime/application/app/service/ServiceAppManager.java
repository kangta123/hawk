package com.oc.hawk.container.runtime.application.app.service;

import com.oc.hawk.api.exception.AppBusinessException;
import com.oc.hawk.common.spring.support.cache.DistributedLock;
import com.oc.hawk.common.spring.support.cache.RedisLock;
import com.oc.hawk.container.api.dto.AddServiceAppDTO;
import com.oc.hawk.container.api.dto.ServiceAppDTO;
import com.oc.hawk.container.api.dto.ServiceAppVersionDTO;
import com.oc.hawk.container.domain.facade.InfrastructureLifeCycleFacade;
import com.oc.hawk.container.runtime.application.app.repository.dao.ServiceAppRepository;
import com.oc.hawk.container.runtime.application.app.repository.dao.ServiceAppVersionRepository;
import com.oc.hawk.container.runtime.application.instance.InstanceConfigUseCase;
import com.oc.hawk.container.runtime.port.driven.facade.feign.BaseGateway;
import com.oc.hawk.container.runtime.port.driven.persistence.po.app.ServiceAppPO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ServiceAppManager {
    private final ServiceAppRepository serviceAppRepository;
    private final ServiceAppVersionRepository serviceAppVersionRepository;
    private final InstanceConfigUseCase configurationManager;
    private final BaseGateway baseGateway;
    private final InfrastructureLifeCycleFacade infrastructureLifeCycleFacade;
    private final StringRedisTemplate stringRedisTemplate;

    @Transactional(rollbackFor = Exception.class)
    public Long addAppVersion(AddServiceAppDTO addServiceAppDTO) {
        ServiceAppPO serviceApp = createServiceAppIfNotExist(addServiceAppDTO);
//
        try (DistributedLock lock = new RedisLock("ServiceApp:" + serviceApp.getId(), stringRedisTemplate)) {
            if (lock.tryLock()) {
//                log.info("Create service app version");
//                AddServiceConfigurationDTO addServiceConfiguration = serviceApp.createServiceConfiguration(addServiceAppDTO.getConfiguration());
//
//                BaseInstanceConfig configuration = configurationManager.createConfiguration(addServiceConfiguration, serviceApp.getNamespace());
//
//                ServiceAppVersionPO serviceAppVersion = ServiceAppVersionPO.createdBy(serviceApp, configuration, addServiceAppDTO);
//                serviceAppVersionRepository.save(serviceAppVersion);
//
//                infrastructureLifeCycleFacade.createEntryPoint(configuration);
//
//                if (addServiceAppDTO.getAppId() == null) {
//                    ApplicationContextHolder.context.publishEvent(new ServiceAppCreatedEvent(configuration, serviceApp.getId()));
//                }
            }
        }


        return serviceApp.getId();
    }

    public ServiceAppDTO getServiceApp(Long id) {
//        ServiceAppPO serviceApp = get(id);
//        ServiceAppDTO result = serviceApp.convert();
//
//        List<ServiceAppVersionDTO> versions = queryAppVersion(id, serviceApp);
//        result.setVersions(versions);
//
//        ProjectDTO project = baseGateway.getProject(serviceApp.getProjectId());
//        result.setProject(project);
//        return result;
        return null;
    }
//
//    public List<ServiceAppDTO> getServiceAppList(Long id) {
//        List<String> appStr = buildJobService.getProjectApps(id);
//        List<ServiceApp> apps = serviceAppRepository.findByAppIn(appStr);
//        Map<String, ServiceAppDTO> appMap = apps.stream().map(ServiceApp::convert).collect(Collectors.toMap(ServiceAppDTO::getApp, Function.identity()));
//
//        return appStr.stream().map(str -> appMap.getOrDefault(str, new ServiceAppDTO(str))).collect(Collectors.toList());
//    }

    private ServiceAppPO createServiceAppIfNotExist(AddServiceAppDTO addServiceAppDTO) {
        return serviceAppRepository.findByApp(addServiceAppDTO.getApp()).orElseGet(() -> {
//            ServiceAppPO app = new ServiceAppPO(addServiceAppDTO);

//            return serviceAppRepository.save(app);
            return null;
        });
    }

    private List<ServiceAppVersionDTO> queryAppVersion(Long id, ServiceAppPO serviceApp) {
//        Map<String, String> labelMap = ImmutableMap.of(
//            IServiceLabelConstants.LABEL_APP, serviceApp.getName(),
//            IServiceLabelConstants.LABEL_NAMESPACE, serviceApp.getNamespace(),
//            IServiceLabelConstants.LABEL_PROJECT, String.valueOf(serviceApp.getProjectId()));
//
//        List<RuntimeInfoDTO> infoList = infrastructureLifeCycleFacade.queryService(labelMap, serviceApp.getNamespace());
//        Map<String, List<RuntimeInfoDTO>> infoMapByName = infoList.stream().collect(Collectors.groupingBy(RuntimeInfoDTO::getName));
//        List<ServiceAppVersionPO> versions = serviceAppVersionRepository.findByAppId(id);
//
//        return versions.stream().map(v -> {
//            ServiceAppVersionDTO version = v.convert();
//            version.setInfo(infoMapByName.getOrDefault(version.getConfiguration().getName(), Lists.newArrayList()));
//            return version;
//        }).collect(Collectors.toList());
        return null;

    }

    private ServiceAppPO get(Long id) {
        return serviceAppRepository.findById(id).orElseThrow(() -> new AppBusinessException("应用服务不存在, id=" + id));
    }

}
