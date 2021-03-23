package com.oc.hawk.traffic.entrypoint.api.dto;

import lombok.Data;

@Data
public class EntryPointHistoryListItemDTO {

    private Long id;
    private String requestTime;
    private Long executeTime;

}
