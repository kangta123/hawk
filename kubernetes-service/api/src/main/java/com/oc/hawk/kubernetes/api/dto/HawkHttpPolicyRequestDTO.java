package com.oc.hawk.kubernetes.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class HawkHttpPolicyRequestDTO {
    String serviceName;
    String namespace;
    private List<HawkHttpPolicyDTO> policy;
    private FaultInjectionDTO fault;
    private List<String> versions;
}
