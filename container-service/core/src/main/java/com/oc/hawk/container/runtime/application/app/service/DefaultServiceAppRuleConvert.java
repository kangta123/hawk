package com.oc.hawk.container.runtime.application.app.service;

import com.oc.hawk.container.domain.model.app.ServiceAppRuleRoute;
import com.oc.hawk.container.runtime.port.driven.persistence.po.app.ServiceAppRulePO;
import com.oc.hawk.kubernetes.api.dto.HawkHttpPolicyDTO;

public class DefaultServiceAppRuleConvert implements ServiceAppRuleConverter {
    @Override
    public HawkHttpPolicyDTO convertHttpRule(ServiceAppRulePO rule) {
        return null;
    }

    private HawkHttpPolicyDTO convert(ServiceAppRuleRoute route) {
//        HawkHttpPolicyDTO hawkHttpTrafficRouteDTO = new HawkHttpPolicyDTO();
//
//        if (route.getWeight() != null && !route.getWeight().isEmpty()) {
//            hawkHttpTrafficRouteDTO.setWeight(route.getWeight());
//        }
//        if (route.getConditions() != null) {
//            HawkHttpTrafficPolicyMatchDTO hawkHttpTrafficPolicyMatchDTO = new HawkHttpTrafficPolicyMatchDTO();
//            route.getConditions().forEach(c -> {
//                switch (c.getType()) {
//                    case header:
//                        hawkHttpTrafficPolicyMatchDTO.addHeader(c.getMode().toString(), c.getKey(), c.getValue());
//                        break;
//                    case uri:
//                        hawkHttpTrafficPolicyMatchDTO.setUri(c.getMode().toString(), c.getValue());
//                        break;
//                    case params:
//                        hawkHttpTrafficPolicyMatchDTO.addParams(c.getMode().toString(), c.getKey(), c.getValue());
//                        break;
//                    case method:
//                        hawkHttpTrafficPolicyMatchDTO.addMethod(c.getValue());
//                        break;
//                    default:
//                        break;
//                }
//            });
//            hawkHttpTrafficRouteDTO.setMatch(hawkHttpTrafficPolicyMatchDTO);
//        }
//
//        return hawkHttpTrafficRouteDTO;
        return null;
    }
}
