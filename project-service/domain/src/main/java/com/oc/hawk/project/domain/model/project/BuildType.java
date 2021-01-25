package com.oc.hawk.project.domain.model.project;

public enum BuildType {
    MAVEN("/build.sh"),
    GRADLE(null),
    NPM(null);

    public String getCommand() {
        return command;
    }

    private String command;

    BuildType(String command) {
        this.command = command;
    }
}
