package com.oc.hawk.container.api.command;

import lombok.Data;

import java.util.Map;

@Data
public class CreateServiceEntryPointCommand {
    private String serviceName;
    private Map<Integer, Integer> extraPorts;
    private Integer innerPort;
}
