package com.oc.hawk.container.runtime.application.app.service;

import com.oc.hawk.container.runtime.port.driven.persistence.po.app.ServiceAppRulePO;
import com.oc.hawk.kubernetes.api.dto.HawkHttpPolicyDTO;

public interface ServiceAppRuleConverter {
    HawkHttpPolicyDTO convertHttpRule(ServiceAppRulePO rule);
}
