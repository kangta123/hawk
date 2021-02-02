package com.oc.hawk.container.domain.model.runtime.info;

import com.oc.hawk.ddd.DomainValueObject;

@DomainValueObject

public class DomainIp {
    private final String ip;

    public DomainIp(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

    @Override
    public String toString() {
        return ip;
    }
}
