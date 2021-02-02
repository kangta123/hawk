package com.oc.hawk.project.domain.config;

import lombok.Data;

@Data
public class GitCodeBaseConfig {
    private String defaultPassword;
    private String defaultUsername;
    private String repoPath;

}
