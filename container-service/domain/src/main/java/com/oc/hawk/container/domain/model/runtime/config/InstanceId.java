package com.oc.hawk.container.domain.model.runtime.config;

import com.oc.hawk.ddd.DomainValueObject;
import lombok.Getter;
import lombok.NoArgsConstructor;

@DomainValueObject
@Getter
@NoArgsConstructor
public class InstanceId {
    long id;

    public InstanceId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
