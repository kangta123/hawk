package com.oc.hawk.traffic.entrypoint.api.dto;

import lombok.Data;

@Data
public class TraceNodeDTO {
    
    private String traceId;
    private String spanId;
    private String parentSpanId;
    private String instanceName;
    private String method;
    private String path;
    private Integer responseCode;
    private Long latency;
    private Long entryPointId;
    private String entryPointName;
    
}
