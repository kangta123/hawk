package com.oc.hawk.kubernetes.api.dto;

import lombok.Data;

@Data
public class FaultInjectionDTO {
    private Integer value;
    private FaultType type;

    public enum FaultType {
        delay, error
    }
}
