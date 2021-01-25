package com.oc.hawk.kubernetes.runtime.application.runtime.spec.deployment.decorator;

import com.oc.hawk.kubernetes.domain.model.IServiceLabelConstants;
import com.oc.hawk.kubernetes.runtime.application.runtime.spec.deployment.ConfigurableRuntimeComponent;
import com.oc.hawk.kubernetes.runtime.application.runtime.spec.deployment.RuntimeComponent;

public class MonitorDecorator extends AbstractConfigurableRuntimeDecorator {
    public MonitorDecorator(ConfigurableRuntimeComponent component) {
        super(component);
    }

    @Override
    public void config(RuntimeComponent runtimeComponent) {
        super.config(runtimeComponent);
        configContainerAnnotations(runtimeComponent);
    }

    private void configContainerAnnotations(RuntimeComponent runtimeComponent) {
        runtimeComponent.addAnnotation(IServiceLabelConstants.ANNOTATION_PROMETHEUS_IO_PATH, "/metrics");
        runtimeComponent.addAnnotation(IServiceLabelConstants.ANNOTATION_PROMETHEUS_IO_PORT, "9001");
        runtimeComponent.addAnnotation(IServiceLabelConstants.ANNOTATION_PROMETHEUS_IO_SCRAPE, "true");
    }
}
