package com.oc.hawk.container.domain.model.runtime.config;

import com.oc.hawk.container.domain.model.runtime.build.ProjectTypeInfo;
import com.oc.hawk.ddd.DomainEntity;
import lombok.Getter;

@Getter
@DomainEntity
public class NginxInstanceConfig implements InstanceConfig {
    private BaseInstanceConfig instanceConfig;
    private String nginxLocation;

    public NginxInstanceConfig(BaseInstanceConfig instanceConfig, String nginxLocation) {
        this.instanceConfig = instanceConfig;
        this.nginxLocation = nginxLocation;
    }

    @Override
    public InstanceConfig getBaseConfig() {
        return instanceConfig;
    }

    @Override
    public String getRuntimeType() {
        return ProjectTypeInfo.NGINX;
    }


    public void update(String nginxLocation) {

        if (nginxLocation != null) {
            this.nginxLocation = nginxLocation;
        }
    }
}
