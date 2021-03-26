package com.oc.hawk.traffic.entrypoint.api.dto;

import lombok.Data;

@Data
public class EntryPointTraceListItemDTO {

    private Long id;
    private String requestTime;
    private Long executeTime;

}
