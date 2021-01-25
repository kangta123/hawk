package com.oc.hawk.kubernetes.api.dto;

import lombok.Data;

import java.util.Map;

@Data
public class HawkHttpPolicyDTO {
    private Map<String, String> weight;
    private HawkHttpTrafficPolicyMatchDTO match;
}
