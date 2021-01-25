package com.oc.hawk.kubernetes.runtime.application.runtime.spec.deployment.component;

import com.google.common.collect.Maps;
import com.oc.hawk.kubernetes.domain.model.IServiceLabelConstants;
import com.oc.hawk.kubernetes.domain.model.KubernetesLabel;
import com.oc.hawk.kubernetes.runtime.application.runtime.spec.RuntimeConfigSpec;
import com.oc.hawk.kubernetes.runtime.application.runtime.spec.deployment.ConfigurableRuntimeComponent;
import com.oc.hawk.kubernetes.runtime.application.runtime.spec.deployment.RuntimeComponent;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@Setter
public class DefaultRuntimeComponent implements ConfigurableRuntimeComponent {
    private RuntimeConfigSpec configuration;

    public DefaultRuntimeComponent(RuntimeConfigSpec configuration) {
        this.configuration = configuration;
    }

    @Override
    public void config(RuntimeComponent runtimeComponent) {
        this.configServiceEnv(runtimeComponent);
        this.configLabels(runtimeComponent);
    }

    private void configServiceEnv(RuntimeComponent componentHolder) {
        Map<String, String> env;
        if (configuration.getEnv() != null) {
            env = configuration.getEnv();
        } else {
            env = Maps.newHashMap();
        }
        componentHolder.addEnvMap(env);
    }

    private void configLabels(RuntimeComponent runtimeComponent) {
        Map<String, String> labelMaps = Maps.newHashMap();

        if (configuration.getProjectId() != null) {
            labelMaps.put(IServiceLabelConstants.LABEL_PROJECT, String.valueOf(configuration.getProjectId()));
            labelMaps.putAll(KubernetesLabel.withBusinessService());
        }

        // For istio doc suggestion https://preliminary.istio.io/zh/docs/ops/deployment/requirements/
        labelMaps.put(IServiceLabelConstants.LABEL_APP, configuration.getServiceName());
        labelMaps.put(IServiceLabelConstants.LABEL_VERSION, configuration.getName());


        runtimeComponent.addLabels(labelMaps);
    }
}
