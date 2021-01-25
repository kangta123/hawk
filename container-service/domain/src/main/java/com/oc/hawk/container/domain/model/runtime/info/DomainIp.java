package com.oc.hawk.container.domain.model.runtime.info;

import com.oc.hawk.ddd.DomainValueObject;

@DomainValueObject

public class DomainIp {
    private String ip;

    public String getIp() {
        return ip;
    }

    public DomainIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return ip;
    }
}
