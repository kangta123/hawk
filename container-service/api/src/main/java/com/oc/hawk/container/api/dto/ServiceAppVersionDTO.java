package com.oc.hawk.container.api.dto;

import com.oc.hawk.kubernetes.api.constants.RuntimeInfoDTO;
import lombok.Data;

import java.util.List;

@Data
public class ServiceAppVersionDTO {
    private Long id;
    private Integer scale;
    private Long appId;
    private InstanceConfigDTO configuration;
    private List<RuntimeInfoDTO> info;

}
