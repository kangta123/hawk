package com.oc.hawk.container.domain.model.runtime.config;

import com.oc.hawk.container.domain.model.runtime.build.ProjectTypeInfo;

import java.util.Map;


public class SpringBootInstanceConfig extends JavaInstanceConfig {
    private String profile;


    public SpringBootInstanceConfig(BaseInstanceConfig baseInstanceConfig, Map<String, String> property, Boolean debug, Boolean jprofiler, String profile) {
        super(baseInstanceConfig, property, debug, jprofiler);
        this.profile = profile;
    }

    public void update(Boolean debug, String profile, Boolean jProfiler, Map<String, String> property) {
        super.update(debug, jProfiler, property);
        if (profile != null) {
            this.profile = profile;
        }
    }

    public String getProfile() {
        return profile;
    }

    @Override
    public String getRuntimeType() {
        return ProjectTypeInfo.JAVA_SPRINGBOOT;
    }
}
