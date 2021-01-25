package com.oc.hawk.container.domain.model.app;

import com.oc.hawk.ddd.DomainValueObject;
import lombok.Getter;

@DomainValueObject
@Getter
public class ServiceAppId {
    private Long id;

    public ServiceAppId(Long id) {
        this.id = id;
    }
}
