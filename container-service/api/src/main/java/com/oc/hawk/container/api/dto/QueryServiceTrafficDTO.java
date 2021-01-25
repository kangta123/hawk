package com.oc.hawk.container.api.dto;

import com.oc.hawk.container.api.constants.Direction;
import lombok.Data;


@Data
public class QueryServiceTrafficDTO {
    private Integer page;
    private Integer size;
    private Direction direction;
    private Integer responseCode;
    private String requestPath;
    private int startTime;
    private int endTime;


}
