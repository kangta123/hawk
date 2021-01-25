package com.oc.hawk.container.domain.model.runtime.config;

import com.oc.hawk.ddd.DomainValueObject;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;

@DomainValueObject
@Getter
@NoArgsConstructor
public class InstanceRemoteAccess {
    private String sshPassword;

    public InstanceRemoteAccess(String sshPassword) {
        this.sshPassword = sshPassword;
    }

    public static InstanceRemoteAccess createNew() {
        return new InstanceRemoteAccess(RandomStringUtils.randomAlphabetic(10));
    }
}
