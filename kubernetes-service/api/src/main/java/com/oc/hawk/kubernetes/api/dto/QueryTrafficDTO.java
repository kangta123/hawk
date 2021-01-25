package com.oc.hawk.kubernetes.api.dto;

import lombok.Data;

@Data
public class QueryTrafficDTO {
    private Integer page;
    private Integer size;
    private String method;
    private String sourceNamespace;
    private String destinationNamespace;
    private String sourceService;
    private String destinationService;
    private String targetService;
    private String excludePath;
    private String requestPath;
    private int startTime;
    private int endTime;
    private Integer responseCode;
}
