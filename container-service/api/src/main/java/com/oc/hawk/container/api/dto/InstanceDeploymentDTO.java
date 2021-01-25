package com.oc.hawk.container.api.dto;

import lombok.Data;

@Data
public class InstanceDeploymentDTO {
    private InstanceConfigDTO instance;
    private Long projectBuildJobId;

}
