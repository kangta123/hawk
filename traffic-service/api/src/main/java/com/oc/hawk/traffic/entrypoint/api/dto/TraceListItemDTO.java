package com.oc.hawk.traffic.entrypoint.api.dto;

import lombok.Data;

@Data
public class TraceListItemDTO {

    private Long id;
    private String requestTime;
    private Long executeTime;
    private String spanId;
    private Integer responseCode;

}
