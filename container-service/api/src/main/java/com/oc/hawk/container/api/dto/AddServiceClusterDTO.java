package com.oc.hawk.container.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class AddServiceClusterDTO {
    private String name;
    private String descn;
    private Long department;
    private List<Long> templateIds;
    private List<Long> clusterIds;
    private Boolean eureka;
    private Boolean publicIp;
    private Boolean autoUpdate;
    private Boolean configServer;
    private String kafkaAddress;
    private String redisAddress;
    private String dbAddress;
    private String dbUsername;
    private String dbPassword;

}
