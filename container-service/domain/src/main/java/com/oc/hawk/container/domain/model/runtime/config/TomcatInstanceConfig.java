package com.oc.hawk.container.domain.model.runtime.config;

import com.oc.hawk.container.domain.model.runtime.build.ProjectTypeInfo;

import java.util.Map;

public class TomcatInstanceConfig extends JavaInstanceConfig {
    public TomcatInstanceConfig(BaseInstanceConfig baseInstanceConfig, Map<String, String> property, Boolean debug, Boolean jprofiler) {
        super(baseInstanceConfig, property, debug, jprofiler);
    }

    @Override
    public String getRuntimeType() {
        return ProjectTypeInfo.JAVA_TOMCAT;
    }
}
