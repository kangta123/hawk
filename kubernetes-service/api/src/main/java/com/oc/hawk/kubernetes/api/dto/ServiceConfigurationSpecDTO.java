package com.oc.hawk.kubernetes.api.dto;

import com.google.common.collect.Maps;
import lombok.Data;

import java.util.Map;

@Data
public class ServiceConfigurationSpecDTO {
    protected Map<Integer, Integer> extraPorts;
    private String namespace;
    private String performanceLevel;
    private Map<String, String> env = Maps.newHashMap();
    private String hosts;
    private String preStart;
    private InstanceVolumeSpecDTO volume;
    private String image;
    private String serviceName;
    private String name;
    private String tag;
    private Integer innerPort;
    private String logPath;
    private Boolean mesh = true;
    private Boolean debug = false;
    private Boolean ssh = false;
    private Boolean jprofiler = false;
    private String sshPassword;
    private Map<String, String> property;
    private String profile;
    private Map<String, String> labels;

    private String healthCheckPath;


}
