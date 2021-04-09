package com.oc.hawk.traffic.entrypoint.domain.model.trace;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class TraceHeaderConfig {
    
    private String keyName;
    private Integer keyType;
    
    public static final Integer REQUEST_TYPE = 1;
    public static final Integer RESPONSE_TYPE = 2;
    
    public TraceHeaderConfig(String keyName,Integer keyType) {
        this.keyName = keyName;
        this.keyType = keyType;
    }
    
}
