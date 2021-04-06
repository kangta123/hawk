package com.oc.hawk.traffic.entrypoint.api.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class TraceItemDTO {
    
    private Long id;
    private String path;
    private String method;
    private String dstWorkload;
    private Integer latency;
    private String spanId;
    private String responseCode;
    private String startTime;
    private Timestamp createTime;
    private Long entryPointId;
    private String entryPointName;
    
}
