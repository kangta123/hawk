package com.oc.hawk.container.runtime.application.app.service;

import com.oc.hawk.container.api.dto.ServiceAppVersionDTO;
import com.oc.hawk.container.domain.facade.InfrastructureLifeCycleFacade;
import com.oc.hawk.container.runtime.application.app.repository.dao.ServiceAppVersionRepository;
import com.oc.hawk.container.runtime.application.instance.InstanceExecutorUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ServiceAppExecutor {
    private final InfrastructureLifeCycleFacade infrastructureLifeCycleFacade;
    private final ServiceAppVersionRepository serviceAppVersionRepository;
    private final InstanceExecutorUseCase instanceExecutorUseCase;

    public void startAppVersion(long versionId) {
//        ServiceAppVersionPO version = serviceAppVersionRepository.findById(versionId).orElseThrow(() -> new AppBusinessException("应用版本不存在, id=" + versionId));
//
//        infrastructureLifeCycleFacade.start(version.getConfig());
//
//        if (version.getScale() > 1) {
//            infrastructureLifeCycleFacade.scale(version);
//        }

    }

    public void stopAppVersion(long versionId) {
//        ServiceAppVersionPO version = serviceAppVersionRepository.findById(versionId).orElseThrow(() -> new AppBusinessException("应用版本不存在, id=" + versionId));
//
//        InstanceConfig config = version.getConfig();
//
//        BaseInstanceConfig baseConfig = (BaseInstanceConfig) config.getBaseConfig();
//        instanceExecutorUseCase.stopService(baseConfig.getId());
//
//        infrastructureLifeCycleFacade.stop(config);

    }

    public ServiceAppVersionDTO scaleApp(long versionId, int value) {
//        ServiceAppVersionPO version = serviceAppVersionRepository.findById(versionId).orElseThrow(() -> new AppBusinessException("应用版本不存在, id=" + versionId));
//
//        ServiceAppPO app = version.getApp();
//
//        version.setScale(value);
//
//        serviceAppVersionRepository.save(version);
//        infrastructureLifeCycleFacade.scale(version);
//
//        ServiceAppVersionDTO result = version.convert();
//;
//
//        List<RuntimeInfoDTO> infoList = infrastructureLifeCycleFacade.queryRuntimeInfo(new InstanceName(app.getName(), app.getNamespace()));
//        result.setInfo(infoList);
//
//        return result;
        return null;
    }

    public void deleteApp(long versionId) {
        this.stopAppVersion(versionId);
        serviceAppVersionRepository.deleteById(versionId);
    }
}
