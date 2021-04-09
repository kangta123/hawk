package com.oc.hawk.traffic.entrypoint.api.dto;

import lombok.Data;

import java.util.Map;

@Data
public class ExecuteResponseDTO {

    private String responseCode;

    private Long responseTime;

    private Map<String, String> responseHeaders;

    private String responseBody;

}
