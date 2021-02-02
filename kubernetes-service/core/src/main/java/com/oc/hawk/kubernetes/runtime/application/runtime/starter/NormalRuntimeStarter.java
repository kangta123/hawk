package com.oc.hawk.kubernetes.runtime.application.runtime.starter;

import com.oc.hawk.kubernetes.runtime.application.runtime.KubernetesRuntimeSpecUseCase;
import com.oc.hawk.kubernetes.runtime.application.runtime.spec.RuntimeConfigSpec;
import com.oc.hawk.kubernetes.runtime.application.runtime.spec.deployment.ConfigurableRuntimeComponent;
import com.oc.hawk.kubernetes.runtime.application.runtime.spec.deployment.NormalRuntimeComponent;
import com.oc.hawk.kubernetes.runtime.application.runtime.spec.deployment.RuntimeComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NormalRuntimeStarter implements ServiceRuntimeStarter {
    private final KubernetesRuntimeSpecUseCase kubernetesRuntimeSpecUseCase;

    @Override
    public void start(RuntimeConfigSpec configuration) {
        RuntimeComponent runtimeComponent = new RuntimeComponent(configuration);

        ConfigurableRuntimeComponent configurableRuntimeComponent = new NormalRuntimeComponent();
        configurableRuntimeComponent.config(runtimeComponent);

        kubernetesRuntimeSpecUseCase.createPod(runtimeComponent);
    }
}
