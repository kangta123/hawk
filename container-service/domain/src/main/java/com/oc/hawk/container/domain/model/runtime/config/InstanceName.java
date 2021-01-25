package com.oc.hawk.container.domain.model.runtime.config;

import com.oc.hawk.ddd.DomainValueObject;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@DomainValueObject
@Getter
public class InstanceName {
    private String name;


    public InstanceName(String name) {
        this.name = name;
    }

    public void validate(String namespace) {
        if (StringUtils.isEmpty(namespace) || StringUtils.isEmpty(getName())) {
            throw new IllegalArgumentException("Namespace and name cannot be null");
        }
    }
}
