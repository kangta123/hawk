package com.oc.hawk.project.api.command;

import lombok.Data;

@Data
public class RegisterProjectCommand {
    private String name;
    private String descn;
    private String projectType;
    private String buildType;
    private Long departmentId;
    private String buildCommand;

    private String url;
    private String token;
    private String username;
    private String password;

    private String output;
}
