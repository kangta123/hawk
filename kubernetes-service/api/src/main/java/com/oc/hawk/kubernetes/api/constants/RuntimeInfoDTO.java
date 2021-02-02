package com.oc.hawk.kubernetes.api.constants;

import lombok.Data;

@Data
public class RuntimeInfoDTO {
    private String id;
    private String podName;
    private String name;
    private String serviceName;
    private String image;
    private String version;
    private String projectId;
    private String namespace;
    private String status;
    private Integer restartCount;
    private Long startTime;
    private String nodeIp;
    private String tag;
}
