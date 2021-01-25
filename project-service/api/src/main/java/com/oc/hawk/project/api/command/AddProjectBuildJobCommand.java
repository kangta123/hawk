package com.oc.hawk.project.api.command;

import lombok.Data;

import java.util.List;

@Data
public class AddProjectBuildJobCommand {
    private String branch;
    private Long projectId;
    private String version;
    private List<Long> appIds;
}
