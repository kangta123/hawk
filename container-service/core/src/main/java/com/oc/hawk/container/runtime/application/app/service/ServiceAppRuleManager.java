package com.oc.hawk.container.runtime.application.app.service;

import com.oc.hawk.api.exception.AppBusinessException;
import com.oc.hawk.common.utils.BeanUtils;
import com.oc.hawk.container.api.dto.AddAppRuleConditionDTO;
import com.oc.hawk.container.api.dto.AddAppRuleDTO;
import com.oc.hawk.container.api.dto.ServiceAppRuleDTO;
import com.oc.hawk.container.domain.facade.InfrastructureLifeCycleFacade;
import com.oc.hawk.container.domain.model.app.ServiceAppRuleCondition;
import com.oc.hawk.container.domain.model.runtime.config.BaseInstanceConfig;
import com.oc.hawk.container.runtime.application.app.repository.dao.ServiceAppRepository;
import com.oc.hawk.container.runtime.application.app.repository.dao.ServiceAppRuleRepository;
import com.oc.hawk.container.runtime.application.app.repository.dao.ServiceAppVersionRepository;
import com.oc.hawk.container.runtime.port.driven.persistence.po.app.ServiceAppPO;
import com.oc.hawk.container.runtime.port.driven.persistence.po.app.ServiceAppRulePO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ServiceAppRuleManager {
    private final ServiceAppRuleRepository appRuleRepository;
    private final InfrastructureLifeCycleFacade infrastructureLifeCycleFacade;
    private final ServiceAppRepository serviceAppRepository;
    private final ServiceAppVersionRepository appVersionRepository;
    private final StringRedisTemplate stringRedisTemplate;

    public void addAppRule(long appId, AddAppRuleDTO addRule) {
//        addRule.validate();
//        ServiceAppPO app = this.getApp(appId);
//
//        ServiceAppRulePO appRule = ServiceAppRulePO.newFromAppVersion(app);
//        AddAppRuleWeightDTO weightRule = addRule.getWeight();
//        appRule.setRoute(ServiceAppRuleRoute.create(weightRule.getWeight(), getConditions(weightRule.getConditions())));
//
//        try (DistributedLock lock = new RedisLock("serviceAppRule:" + appRule.getApp().getId(), stringRedisTemplate)) {
//            if (lock.tryLock()) {
//                int order = appRuleRepository.getMaxAppRuleOrder(appId).orElse(0) + 1;
//                appRule.setOrder(order);
//                appRuleRepository.save(appRule);
//            }
//        }

    }

    private List<ServiceAppRuleCondition> getConditions(List<AddAppRuleConditionDTO> conditions) {
        return BeanUtils.batchTransform(ServiceAppRuleCondition.class, conditions);
    }

    public List<ServiceAppRuleDTO> getAppRule(long id) {
//        List<ServiceAppRulePO> appRules = appRuleRepository.findByAppId(id);
//        return appRules.stream().sorted().map(ServiceAppRulePO::convert).collect(Collectors.toList());
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateAppRuleStatus(long id, Boolean enable) {
        ServiceAppRulePO appRule = getServiceAppRule(id);
        appRule.setEnabled(enable);
    }

    private ServiceAppRulePO getServiceAppRule(long id) {
        return appRuleRepository.findById(id).orElseThrow(() -> new AppBusinessException("应用规则不存在"));
    }

    @Transactional(rollbackFor = Exception.class)
    public void exchangeAppRuleOrder(long id, Long targetId) {
//        ServiceAppRulePO serviceAppRule = getServiceAppRule(id);
//        ServiceAppRulePO target = getServiceAppRule(targetId);
//        serviceAppRule.exchangeOrder(target);
    }

    public void deleteAppRule(long app, long rule) {
        if (appRuleRepository.countByAppId(app) == 1) {
            throw new AppBusinessException("至少要保留一个路由规则");
        }
        appRuleRepository.deleteById(rule);
    }

    public void applyAppRule(long appId) {
//        ServiceAppPO app = getApp(appId);
//        List<ServiceAppRulePO> appRules = appRuleRepository.findByAppIdAndEnabled(app.getId(), true);
//        List<String> versionNames = appVersionRepository.getVersionName(appId);
//
//        appRules.sort(Comparator.comparingInt(ServiceAppRulePO::getOrder));
//
//        infrastructureLifeCycleFacade.applyServiceAppRules(app, versionNames, appRules);
    }

    private ServiceAppPO getApp(long appId) {
        return serviceAppRepository.findById(appId).orElseThrow(() -> new AppBusinessException("应用不存在"));
    }

    public void applyDefaultAppRuleIfEmpty(BaseInstanceConfig configuration, Long appId) {
        boolean ruleExisted = appRuleRepository.existsByAppId(appId);
        if (!ruleExisted) {
//            ServiceAppRulePO defaultAppRule = ServiceAppRulePO.createDefaultAppRule(configuration, appId);
//            appRuleRepository.save(defaultAppRule);
//            this.applyAppRule(appId);
        }
    }
}
