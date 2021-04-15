package com.oc.hawk.traffic.entrypoint.api.dto;

import lombok.Data;

@Data
public class TraceListItemDTO {

    private Long id;
    private Long requestTime;
    private Long executeTime;
    private String spanId;
    private Integer responseCode;

}
