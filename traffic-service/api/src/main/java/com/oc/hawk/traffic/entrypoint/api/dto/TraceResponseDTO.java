package com.oc.hawk.traffic.entrypoint.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class TraceResponseDTO {
    private Long totalSize;
    private List<TraceListItemDTO> items;
}
