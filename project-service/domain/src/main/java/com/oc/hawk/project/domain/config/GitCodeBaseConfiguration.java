package com.oc.hawk.project.domain.config;

import lombok.Data;

@Data
public class GitCodeBaseConfiguration {
    private String defaultPassword;
    private String defaultUsername;
    private String repoPath;

}
