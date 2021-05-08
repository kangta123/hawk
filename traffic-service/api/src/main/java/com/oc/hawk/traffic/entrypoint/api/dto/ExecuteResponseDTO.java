package com.oc.hawk.traffic.entrypoint.api.dto;

import lombok.Data;

import java.util.Map;

@Data
public class ExecuteResponseDTO {

    private String responseCode;
    private Long latency;

    private Map<String, String> responseHeaders;

    private String responseBody;

}
