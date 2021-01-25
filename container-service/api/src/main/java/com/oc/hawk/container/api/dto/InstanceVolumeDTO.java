package com.oc.hawk.container.api.dto;

import lombok.Data;

@Data
public class InstanceVolumeDTO {
    private String mountPath;
    private String volumeName;
}
