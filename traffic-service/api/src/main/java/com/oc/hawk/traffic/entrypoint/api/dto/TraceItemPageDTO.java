package com.oc.hawk.traffic.entrypoint.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class TraceItemPageDTO {

    private boolean hasNext;
    private List<TraceItemDTO> items;

}
