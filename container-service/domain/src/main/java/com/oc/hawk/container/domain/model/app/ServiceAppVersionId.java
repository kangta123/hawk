package com.oc.hawk.container.domain.model.app;

import com.oc.hawk.ddd.DomainEntity;
import lombok.Getter;

@Getter
@DomainEntity
public class ServiceAppVersionId {
    private Long id;

    public ServiceAppVersionId(Long id) {
        this.id = id;
    }
}
