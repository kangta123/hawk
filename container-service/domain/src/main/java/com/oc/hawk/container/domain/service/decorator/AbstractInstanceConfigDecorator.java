package com.oc.hawk.container.domain.service.decorator;

import com.oc.hawk.container.domain.model.runtime.config.InstanceConfig;

public abstract class AbstractInstanceConfigDecorator implements InstanceConfigRuntimeDecorator {
    protected static final String ENABLED = "1";
    protected static final String DISABLED = "0";
    private final InstanceConfigRuntimeDecorator configRuntimeDecorator;

    public AbstractInstanceConfigDecorator(InstanceConfigRuntimeDecorator configRuntimeDecorator) {
        this.configRuntimeDecorator = configRuntimeDecorator;
    }

    @Override
    public void config(InstanceConfig instanceConfig) {
        if (configRuntimeDecorator != null) {
            this.configRuntimeDecorator.config(instanceConfig);
        }
    }

}
