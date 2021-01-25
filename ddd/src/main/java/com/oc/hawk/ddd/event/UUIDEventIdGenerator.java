package com.oc.hawk.ddd.event;

import java.util.UUID;

public class UUIDEventIdGenerator implements EventIdGenerator{
    @Override
    public String id() {
        return newUuid();
    }


    private String newUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
