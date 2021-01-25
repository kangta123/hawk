package com.oc.hawk.kubernetes.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RuntimeStartFailedDto {
    private Long projectId;
    private String instanceName;
    private String namespace;

    public RuntimeStartFailedDto(Long projectId, String instanceName, String namespace) {
        this.projectId = projectId;
        this.instanceName = instanceName;
        this.namespace = namespace;
    }
}
