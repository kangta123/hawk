package com.oc.hawk.container.api.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class AddAppRuleWeightDTO {
    private List<AddAppRuleConditionDTO> conditions;
    private Map<String, String> weight;
}
