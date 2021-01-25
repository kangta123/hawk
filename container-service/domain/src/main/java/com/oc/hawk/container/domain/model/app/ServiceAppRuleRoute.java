package com.oc.hawk.container.domain.model.app;


import java.util.List;
import java.util.Map;


public class ServiceAppRuleRoute {
    private List<ServiceAppRuleCondition> conditions;
    /**
     * map<version, weight>
     */
    private Map<String, String> weight;

    public static ServiceAppRuleRoute create(Map<String, String> weight, List<ServiceAppRuleCondition> conditions) {
        ServiceAppRuleRoute rule = new ServiceAppRuleRoute();
        rule.weight = weight;
        rule.conditions = conditions;
        return rule;
    }

}
