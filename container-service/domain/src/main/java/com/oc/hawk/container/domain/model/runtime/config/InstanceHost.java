package com.oc.hawk.container.domain.model.runtime.config;

import com.google.common.collect.Maps;
import com.oc.hawk.ddd.DomainValueObject;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@DomainValueObject
@Getter
@Builder(access = AccessLevel.PACKAGE)
public class InstanceHost {
    private Map<String, String> env;
    private String hosts;
    private String preStart;
    private InstanceRemoteAccess remoteAccess;

    public void update(String hosts, String preStart, Boolean ssh, Map<String, String> env) {
        if (hosts != null) {
            this.hosts = hosts;
        }
        if (preStart != null) {
            this.preStart = preStart;
        }
        if (ssh != null) {
            if (ssh) {
                remoteAccess = InstanceRemoteAccess.createNew();
            } else {
                remoteAccess = null;
            }
        }

        if (env != null) {
            this.env = env;
        }
    }

    public void addEnv(String key, String value) {
        if (this.env == null) {
            this.env = Maps.newHashMap();
        }

        this.env.put(key, value);
    }
}
