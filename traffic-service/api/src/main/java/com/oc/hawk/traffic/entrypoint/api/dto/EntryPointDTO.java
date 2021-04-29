package com.oc.hawk.traffic.entrypoint.api.dto;

import lombok.Data;

@Data
public class EntryPointDTO {

    private Long id;
    private Long groupId;
    private String apiName;
    private String apiPath;
    private String apiMethod;
    private String apiDesc;
    private String projectId;
    private String app;
    private String projectName;

}
