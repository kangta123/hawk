package com.oc.hawk.kubernetes.runtime.application.runtime.spec.deployment;

import com.oc.hawk.container.domain.config.HealthCheckProperties;
import com.oc.hawk.kubernetes.runtime.application.runtime.spec.RuntimeConfigSpec;
import com.oc.hawk.kubernetes.runtime.application.runtime.spec.deployment.component.DefaultRuntimeComponent;
import com.oc.hawk.kubernetes.runtime.application.runtime.spec.deployment.decorator.MonitorDecorator;
import com.oc.hawk.kubernetes.runtime.application.runtime.spec.deployment.decorator.ServiceMeshDecorator;
import com.oc.hawk.kubernetes.runtime.application.runtime.spec.deployment.decorator.ServiceVolumeDecorator;

public class BusinessRuntimeComponent implements ConfigurableRuntimeComponent {


    @Override
    public void config(RuntimeComponent componentHolder) {
        RuntimeConfigSpec configuration = componentHolder.getConfiguration();

        ConfigurableRuntimeComponent configurableRuntimeComponent = new DefaultRuntimeComponent(configuration);
        configurableRuntimeComponent = new ServiceMeshDecorator(configurableRuntimeComponent);
        configurableRuntimeComponent = new MonitorDecorator(configurableRuntimeComponent);
        configurableRuntimeComponent = new ServiceVolumeDecorator(configurableRuntimeComponent);

        configurableRuntimeComponent.config(componentHolder);

    }
}
