package com.oc.hawk.container.api.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ServiceAppRuleRouteDTO {
    private List<ServiceAppRuleConditionDTO> conditions;
    Map<String, String> weight;
    private String name;
}
