package com.oc.hawk.container.domain.model.runtime.config;

import com.google.common.collect.Maps;
import com.oc.hawk.container.domain.model.runtime.SystemServicePort;
import com.oc.hawk.ddd.DomainEntity;

import java.util.Map;

@DomainEntity
public abstract class JavaInstanceConfig implements InstanceConfig {
    private final String RUNTIME_MARK_KEY = "service_name";
    private final BaseInstanceConfig baseInstanceConfig;
    private Map<String, String> property;
    private Boolean debug;
    private Boolean jprofiler;

    public JavaInstanceConfig(BaseInstanceConfig baseInstanceConfig, Map<String, String> property, Boolean debug, Boolean jprofiler) {
        this.baseInstanceConfig = baseInstanceConfig;
        this.property = property;
        this.debug = debug;
        this.jprofiler = jprofiler;
        if (Boolean.TRUE.equals(debug)) {
            baseInstanceConfig.exposePort(SystemServicePort.DEBUG_PORT);
        }
        if (Boolean.TRUE.equals(jprofiler)) {
            baseInstanceConfig.exposePort(SystemServicePort.JPROFILER_PORT);
        }
    }

    @Override
    public InstanceId getId() {
        return baseInstanceConfig.getId();
    }

    @Override
    public InstanceConfig getBaseConfig() {
        return baseInstanceConfig;
    }

    public void update(Boolean debug, Boolean jProfiler, Map<String, String> property) {
        if (debug != null) {
            this.debug = debug;
            if (!debug) {
                this.baseInstanceConfig.discardNetworkPort(SystemServicePort.DEBUG_PORT);
            }
        }

        if (jProfiler != null) {
            this.jprofiler = jProfiler;
            if (!jprofiler) {
                this.baseInstanceConfig.discardNetworkPort(SystemServicePort.JPROFILER_PORT);
            }
        }

        if (property != null) {
            this.property = property;
        }

    }

    public Map<String, String> getJavaConfigProperty(String serviceName) {
        if (property == null) {
            property = Maps.newHashMap();
        }

        property.put(RUNTIME_MARK_KEY, serviceName);
        return property;
    }

    public BaseInstanceConfig getBaseInstanceConfig() {
        return baseInstanceConfig;
    }

    public Boolean getDebug() {
        return debug;
    }

    public Boolean getJprofiler() {
        return jprofiler;
    }

    public Map<String, String> getProperty() {
        return property;
    }
}
