package com.oc.hawk.kubernetes.api.dto;

import lombok.Data;

import java.util.Map;

@Data
public class ServiceEntryPointDTO {
    private String namespace;
    private String serviceName;
    private String name;
    private Map<Integer, Integer> extraPorts;
    private Integer innerPort;
}
