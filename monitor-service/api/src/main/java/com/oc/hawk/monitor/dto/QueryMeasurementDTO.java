package com.oc.hawk.monitor.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QueryMeasurementDTO {
    private String namespace;
    //    private String pod;
    private String app;
    private String version;
    private String name;
    //    private List<Long> groupIds;
    private String groupName;
    //    private List<String> metrics;
    private LocalDateTime start;
    private LocalDateTime end;
}
