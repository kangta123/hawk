package com.oc.hawk.traffic.entrypoint.api.dto;

import java.util.List;

import lombok.Data;

@Data
public class TraceResponseDTO {
    private Long totalSize;
    private List<TraceListItemDTO> items;
}
