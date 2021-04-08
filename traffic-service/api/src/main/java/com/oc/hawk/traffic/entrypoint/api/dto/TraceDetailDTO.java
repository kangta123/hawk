package com.oc.hawk.traffic.entrypoint.api.dto;

import java.sql.Timestamp;
import java.util.Map;

import lombok.Data;

@Data
public class TraceDetailDTO {
	
    private Long id;
	private String host;
    private String path;
    private String method;
    private String requestId;
    private String destAddr;
    private String sourceAddr;
    private String dstWorkload;
    private String dstNamespace;
    private Integer latency;
    private String protocol;
    private String spanId;
    private String parentSpanId;
    private String traceId;
    private String requestBody;
    private String requestHeaders;
    private String responseCode;
    private String responseBody;
    private String responseHeaders;
    private Long startTime;
    private Long entryPointId;
    private String entryPointName;
    
    private Map<String, String> requestParams;
    private Map<String, String> uriParams;
	
}
