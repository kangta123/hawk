package com.oc.hawk.container.api.dto;

import lombok.Data;

@Data
public class AddAppRuleConditionDTO {
    private String type;
    private String mode;
    private String key;
    private String value;
}
