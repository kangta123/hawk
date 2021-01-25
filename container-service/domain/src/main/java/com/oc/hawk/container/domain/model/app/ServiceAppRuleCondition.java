package com.oc.hawk.container.domain.model.app;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceAppRuleCondition {
    private RuleItemType type;
    private RuleItemMode mode;
    private String key;
    private String value;

}
