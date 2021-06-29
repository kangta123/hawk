package com.oc.hawk.container.domain.model.runtime;

import lombok.Getter;

@Getter
public enum SystemServicePort {
    DEBUG_PORT(5005),
    SERVICE_PORT(8080),
    JPROFILER_PORT(8849),
    SSH_PORT(22),
    JVM_PORT(4295),
    ANY_PORT(0);
    private final Integer port;

    SystemServicePort(Integer port) {
        this.port = port;
    }

    public static boolean isAnyPort(int port) {
        return ANY_PORT.port == port;
    }

    public static boolean isSystemPort(int port) {
        for (SystemServicePort systemPort : SystemServicePort.values()) {
            if (systemPort.getPort() == port) {
                return true;
            }
        }
        return false;
    }

    public static SystemServicePort port(int port) {
        for (SystemServicePort systemPort : SystemServicePort.values()) {
            if (systemPort.getPort() == port) {
                return systemPort;
            }
        }
        return null;
    }
}
