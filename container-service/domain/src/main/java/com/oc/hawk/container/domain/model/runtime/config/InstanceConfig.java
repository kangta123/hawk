package com.oc.hawk.container.domain.model.runtime.config;


public interface InstanceConfig {
    InstanceId getId();

    InstanceConfig getBaseConfig();

    String getRuntimeType();

}
