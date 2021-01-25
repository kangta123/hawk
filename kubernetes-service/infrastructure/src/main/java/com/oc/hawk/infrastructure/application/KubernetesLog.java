package com.oc.hawk.infrastructure.application;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Getter
public class KubernetesLog {
    private static final String EMPTY_DATA = "";
    private final int timestamp;
    private final String data;

    public KubernetesLog(int timestamp, String data) {
        this.timestamp = timestamp;
        this.data = data;
    }

    public static KubernetesLog empty() {
        int now = (int) LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
        return new KubernetesLog(now, EMPTY_DATA);
    }
}
