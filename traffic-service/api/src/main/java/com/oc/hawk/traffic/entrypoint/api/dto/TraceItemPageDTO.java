package com.oc.hawk.traffic.entrypoint.api.dto;

import java.util.List;
import lombok.Data;

@Data
public class TraceItemPageDTO {
    
    private boolean hasNext;
    private List<TraceItemDTO> items;
    
}
