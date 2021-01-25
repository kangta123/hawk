package com.oc.hawk.kubernetes.api.dto;

import lombok.Data;

@Data
public class MetricRequestDTO {
    int page;
    int size;
    String sourceNamespace;
    String sourceServiceName;
    String method;
    int responseCode;
}
