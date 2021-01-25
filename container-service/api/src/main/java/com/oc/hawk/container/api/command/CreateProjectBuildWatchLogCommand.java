package com.oc.hawk.container.api.command;

import lombok.Data;

@Data
public class CreateProjectBuildWatchLogCommand {
    private String namespace;
    private String name;
    private long domainId;
}
