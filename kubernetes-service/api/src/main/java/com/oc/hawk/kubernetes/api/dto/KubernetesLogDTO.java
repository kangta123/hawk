package com.oc.hawk.kubernetes.api.dto;

import lombok.Data;

@Data
public class KubernetesLogDTO {
    private int timestamp;
    private String data;
}
