package com.oc.hawk.kubernetes.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class MetricResultDTO {
    private int total;
    private List<Content> content;

    @Data
    public static class Content {
        String id;
        String destinationIp;
        String destinationName;
        String destinationNamespace;
        int destinationPort;
        String destinationService;
        String kind;
        int latency;
        String protocol;
        String requestMethod;
        String requestPath;
        int responseCode;
        int responseSize;
        String sourceName;
        String sourceService;
        String xForwardedFor;
        int startTime;
        String userAgent;
        String requestHost;
    }
}

