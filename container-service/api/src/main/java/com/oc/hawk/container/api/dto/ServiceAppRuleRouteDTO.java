package com.oc.hawk.container.api.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ServiceAppRuleRouteDTO {
    Map<String, String> weight;
    private List<ServiceAppRuleConditionDTO> conditions;
    private String name;
}
