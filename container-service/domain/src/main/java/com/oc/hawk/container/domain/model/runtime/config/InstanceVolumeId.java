package com.oc.hawk.container.domain.model.runtime.config;

import com.oc.hawk.ddd.DomainValueObject;
import lombok.Getter;

@DomainValueObject
@Getter
public class InstanceVolumeId {
    private Long id;

    public InstanceVolumeId(Long id) {
        this.id = id;
    }
}
