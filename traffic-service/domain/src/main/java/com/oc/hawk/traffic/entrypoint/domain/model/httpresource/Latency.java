package com.oc.hawk.traffic.entrypoint.domain.model.httpresource;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Latency {
    
    private Long latency;
    private static final Long TIME_UNIT = 1000000L;
    
    public Latency(Long latency) {
        this.latency = latency;
    }
    
    public Long getTime() {
        return this.latency/TIME_UNIT;
    }
    
}
