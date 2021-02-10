package com.oc.hawk.container.api.command;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CreateRuntimeInfoSpecCommand {
    private String namespace;
    private String performance;
    private Map<String, String> env = Maps.newHashMap();
    private String preStart;
    private String dataImage;
    private String name;
    private Boolean mesh = true;
    private Map<String, String> labels;
    private Long projectId;
    private String healthCheckPath;
    private Boolean healthCheckEnabled;

    private List<CreateInstanceVolumeSpecCommand> volume = Lists.newArrayList();
    private CreateInstanceVolumeSpecCommand appVolume;
    private CreateServiceEntryPointCommand entryPoint;
    private String runtimeCatalog;
    private String appImage;
    private Long instanceId;
    private Boolean watchLog;
}
