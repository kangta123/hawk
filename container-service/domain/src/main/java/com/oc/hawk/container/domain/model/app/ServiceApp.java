package com.oc.hawk.container.domain.model.app;

import com.oc.hawk.ddd.DomainEntity;
import lombok.Getter;

@DomainEntity
@Getter
public class ServiceApp {
    private ServiceAppId id;
    private Long projectId;
    private String name;
    private String descn;
    private String app;
    private String namespace;
}
