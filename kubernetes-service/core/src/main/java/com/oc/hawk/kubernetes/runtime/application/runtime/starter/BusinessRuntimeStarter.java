package com.oc.hawk.kubernetes.runtime.application.runtime.starter;

import com.oc.hawk.container.domain.config.HealthCheckProperties;
import com.oc.hawk.kubernetes.runtime.application.runtime.KubernetesRuntimeSpecUseCase;
import com.oc.hawk.kubernetes.runtime.application.runtime.spec.RuntimeConfigSpec;
import com.oc.hawk.kubernetes.runtime.application.runtime.spec.deployment.BusinessRuntimeComponent;
import com.oc.hawk.kubernetes.runtime.application.runtime.spec.deployment.ConfigurableRuntimeComponent;
import com.oc.hawk.kubernetes.runtime.application.runtime.spec.deployment.RuntimeComponent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class BusinessRuntimeStarter implements ServiceRuntimeStarter {
    private final KubernetesRuntimeSpecUseCase kubernetesRuntimeSpecUseCase;

    @Override
    public void start(RuntimeConfigSpec configuration) {
        log.info("Start service {} in {} namespace", configuration.getName(), configuration.getNamespace());

        ConfigurableRuntimeComponent configurableRuntimeComponent = new BusinessRuntimeComponent();

        RuntimeComponent componentHolder = new RuntimeComponent(configuration);

        configurableRuntimeComponent.config(componentHolder);

        kubernetesRuntimeSpecUseCase.createDeployment(componentHolder);

    }

}
