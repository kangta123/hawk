package com.oc.hawk.project.api.command;

import lombok.Data;

import java.util.List;

@Data
public class CreateProjectBuildJobCommand {
    private String branch;
    private Long projectId;
    private String version;
    private Long config;
    private String configName;

    private List<Long> appIds;
}
