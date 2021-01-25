package com.oc.hawk.container.api.dto;

import com.oc.hawk.container.api.constants.AppRuleType;
import lombok.Data;

@Data
public class ServiceAppRuleDTO {
    private Long id;
    private String name;
    private AppRuleType type;
    private ServiceAppRuleRouteDTO route;
    private Boolean enabled;
    private Integer order;

}
