package com.oc.hawk.kubernetes.runtime.application.entrypoint;

import lombok.Getter;

@Getter
public enum ServicePortEnum {
    DEBUG_INNER_PORT("5005"),
    JPROFILER_INNER_PORT("8849"),
    SSH_INNER_PORT("22"),
    ANY_PORT("0");
    private String port;
    ServicePortEnum(String port) {
        this.port = port;
    }

    public static boolean isAnyPort(String port) {
        return ANY_PORT.port.equals(port);
    }
}
