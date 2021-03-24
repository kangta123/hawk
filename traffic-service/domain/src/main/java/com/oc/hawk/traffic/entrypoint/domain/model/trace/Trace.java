package com.oc.hawk.traffic.entrypoint.domain.model.trace;

import java.util.Map;
import com.oc.hawk.ddd.DomainEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@DomainEntity
public class Trace {

    private final TraceId historyId;
    private String host;
    private String path;
    private String destAddr;
    private String sourceAddr;
    private String dstWorkload;
    private String dstNamespace;
    private Long timestamp;
    private Integer latency;
    private String requestId;
    private String protocol;
    private String method;
    private Map<String,String> requestHeaders;
    private Map<String,String> responseHeaders;
    private String responseBody;
    private String requestBody;
    private String spanId;
    private String traceId;
    private String responseCode;
    private Long configId;
    
}

