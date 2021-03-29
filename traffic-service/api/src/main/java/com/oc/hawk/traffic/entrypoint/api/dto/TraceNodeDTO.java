package com.oc.hawk.traffic.entrypoint.api.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class TraceNodeDTO {
    
    private String traceId;
    private String spanId;
    private String parentSpanId;
    private String instanceName;
    private String method;
    private String path;
    private String responseCode;
    private Integer latency;
    private Long entryPointId;
    private String entryPointName;
    
    private List<TraceNodeDTO> childNodeList = new ArrayList<>();
    
}
