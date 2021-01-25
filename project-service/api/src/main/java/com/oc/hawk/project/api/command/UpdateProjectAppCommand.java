package com.oc.hawk.project.api.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProjectAppCommand {
    private Long projectId;
    private String branch;
    private String appName;
    private String appPath;
}
