package com.oc.hawk.container.api.dto;

import lombok.Data;

@Data
public class InstanceProjectDTO {

    private Long id;
    private String app;
    private String instanceName;
    private Boolean enabled;

}
