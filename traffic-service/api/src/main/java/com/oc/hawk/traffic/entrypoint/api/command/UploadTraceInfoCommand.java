package com.oc.hawk.traffic.entrypoint.api.command;

import lombok.Data;

import java.util.Map;

@Data
public class UploadTraceInfoCommand {
    
    private String host;
    private String path;
    private String destAddr;
    private String sourceAddr;
    private String dstWorkload;
    private String dstNamespace;
    private Long timestamp;
    private Long latency;
    private String requestId;
    private String protocol;
    private String method;
    private Map<String,String> requestHeaders;
    private Map<String,String> responseHeaders;
    private String responseBody;
    private String requestBody;
    private String spanId;
    private String parentSpanId;
    private String traceId;
    private String responseCode;
    private String kind;

}
