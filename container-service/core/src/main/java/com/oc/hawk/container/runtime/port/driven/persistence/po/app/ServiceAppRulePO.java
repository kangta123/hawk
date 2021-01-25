package com.oc.hawk.container.runtime.port.driven.persistence.po.app;

import com.oc.hawk.common.hibernate.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "container_service_app_rules")
@Getter
@Setter
public class ServiceAppRulePO extends BaseEntity {
    private static final String DEFAULT_WEIGHT = "100";

    private Long appId;
    private String route;

    private boolean enabled;
    private int order;


//    public static ServiceAppRulePO newFromAppVersion(ServiceAppPO app) {
//        ServiceAppRulePO appRule = new ServiceAppRulePO();
//        appRule.setApp(app);
//        appRule.setEnabled(false);
//        return appRule;
//    }
//
//    public static ServiceAppRulePO createDefaultAppRule(BaseInstanceConfig configuration, Long appId) {
//        ServiceAppRulePO appRule = new ServiceAppRulePO();
//
//        ServiceAppPO app = new ServiceAppPO();
//        app.setId(appId);
//
//        appRule.setApp(app);
//        appRule.setOrder(0);
//        appRule.setRoute(ServiceAppRuleRoute.create(ImmutableMap.of(configuration.getName(), DEFAULT_WEIGHT), null));
//        appRule.setEnabled(true);
//        return appRule;
//    }
//
//
}
