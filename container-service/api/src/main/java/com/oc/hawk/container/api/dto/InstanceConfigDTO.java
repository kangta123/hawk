package com.oc.hawk.container.api.dto;

import com.oc.hawk.kubernetes.api.constants.RuntimeInfoDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class InstanceConfigDTO {
    private Long id;
    private Long projectId;
    private String app;
    private String serviceName;
    private String name;
    private String domainHost;
    private String profile;
    private String tag;
    private Map<String, String> env;
    private String branch;
    private String performanceLevel;
    private Boolean debug;
    private Boolean mesh;
    private Boolean jprofiler;
    private String descn;
    private Boolean ssh;
    private String sshPassword;
    private String hosts;
    private String preStart;
    private Map<String, String> property;
    private Map<Integer, Integer> customExposePorts;
    private Map<Integer, Integer> exposePorts;
    private List<InstanceManagerDTO> managers;
    private InstanceVolumeDTO volume;
    private String logPath;
    private String namespace;
    private Integer innerPort;

    private RuntimeInfoDTO containerInfo;

    private String nginxLocation;
    private String healthCheckPath;
    private LocalDateTime updatedTime;
    private LocalDateTime createdTime;

}
