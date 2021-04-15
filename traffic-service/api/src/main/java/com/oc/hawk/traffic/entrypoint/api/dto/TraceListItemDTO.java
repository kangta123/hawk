package com.oc.hawk.traffic.entrypoint.api.dto;

import lombok.Data;

@Data
public class TraceListItemDTO {

    private Long id;
    private Long startTime;
    private Long latency;
    private String spanId;
    private Integer responseCode;

}
