package com.oc.hawk.container.api.command;

import com.oc.hawk.container.api.dto.InstanceVolumeDTO;
import lombok.Data;

import java.util.List;

@Data
public class ChangeInstanceConfigCommand {
    private List<String> envKey;
    private List<String> envValue;
    private List<String> propertyKey;
    private List<String> propertyValue;
    private String profile;
    private Integer innerPort;
    private String logPath;
    private Boolean debug;
    private Boolean ssh;
    private Boolean jprofiler;
    private Boolean mesh;
    private String descn;
    private List<Integer> exposePorts;
    private String hosts;
    private String preStart;
    private String performanceLevel;
    private List<Long> managerIds;
    private InstanceVolumeDTO volume;
    private String tag;
    private String app;
    private String healthCheckPath;
    private Boolean healthCheck;
    private String nginxLocation;
}
