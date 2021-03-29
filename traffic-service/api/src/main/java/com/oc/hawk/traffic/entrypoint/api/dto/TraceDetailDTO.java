package com.oc.hawk.traffic.entrypoint.api.dto;

import java.sql.Timestamp;
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
    private Timestamp createTime;
    private Long entryPointId;
    private String entryPointName;
	
}
