package com.oc.hawk.container.domain.model.app;

import com.oc.hawk.container.domain.model.runtime.config.InstanceConfig;
import com.oc.hawk.ddd.DomainEntity;
import lombok.Getter;

@DomainEntity
@Getter
public class ServiceAppVersion {
    private ServiceAppVersionId id;
    private ServiceApp app;
    private String name;
    private InstanceConfig config;
    private Integer scale;

}
