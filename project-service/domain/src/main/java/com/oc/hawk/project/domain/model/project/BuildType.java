package com.oc.hawk.project.domain.model.project;

public enum BuildType {
    MAVEN("/build.sh"),
    GRADLE(null),
    NPM(null);

    private final String command;

    BuildType(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}
