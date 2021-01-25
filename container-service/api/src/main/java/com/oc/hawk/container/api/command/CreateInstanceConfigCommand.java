package com.oc.hawk.container.api.command;

import lombok.Data;

@Data
public class CreateInstanceConfigCommand extends ChangeInstanceConfigCommand {
    private String serviceName;
    private String name;
    private Long projectId;
}
