package com.oc.hawk.kubernetes.runtime.application.runtime.spec.deployment.decorator;

import com.oc.hawk.kubernetes.runtime.application.runtime.spec.deployment.ConfigurableRuntimeComponent;
import com.oc.hawk.kubernetes.runtime.application.runtime.spec.deployment.RuntimeComponent;

public abstract class AbstractConfigurableRuntimeDecorator implements ConfigurableRuntimeComponent {
    private ConfigurableRuntimeComponent component;

    public AbstractConfigurableRuntimeDecorator(ConfigurableRuntimeComponent component) {
        this.component = component;
    }

    @Override
    public void config(RuntimeComponent runtimeComponent) {
        if (component != null) {
            component.config(runtimeComponent);
        }
    }
}

