package com.oc.hawk.traffic.entrypoint.api.command;

import lombok.Data;

import java.util.List;

@Data
public class UploadTraceInfoCommand {

    private Long start;
    private Long end;
    private String requestBody;
    private String responseBody;
    private List<List<String>> requestHeaders;
    private List<List<String>> responseHeaders;

}
