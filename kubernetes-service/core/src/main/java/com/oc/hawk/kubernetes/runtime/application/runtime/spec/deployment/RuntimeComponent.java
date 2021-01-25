package com.oc.hawk.kubernetes.runtime.application.runtime.spec.deployment;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.oc.hawk.kubernetes.runtime.application.runtime.ServiceVolumeComponent;
import com.oc.hawk.kubernetes.runtime.application.runtime.spec.RuntimeConfigSpec;
import com.oc.hawk.kubernetes.runtime.application.runtime.spec.ServiceVolumeSpec;
import com.oc.hawk.kubernetes.runtime.application.runtime.spec.deployment.pod.BusinessPodCreator;
import com.oc.hawk.kubernetes.runtime.application.runtime.spec.deployment.pod.PodCreator;
import com.oc.hawk.kubernetes.runtime.application.runtime.spec.deployment.pod.SimplePodCreator;
import io.fabric8.kubernetes.api.model.EnvVar;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class RuntimeComponent {
    private final Map<String, String> labels;
    private final Map<String, String> env;
    private final Map<String, String> annotations;
    private final RuntimeConfigSpec configuration;
    private final ServiceVolumeComponent volumeComponent;

    public RuntimeComponent(RuntimeConfigSpec configuration) {
        this.configuration = configuration;
        this.volumeComponent = new ServiceVolumeComponent(configuration.getVolumes());
        this.labels = configuration.getLabels();

        this.annotations = Maps.newHashMap();
        this.env = configuration.getEnv();
    }

    public void addEnv(String key, String value) {
        env.put(key, value);
    }

    public void addEnvMap(Map<String, String> map) {
        env.putAll(map);
    }

    public void addAnnotation(String key, String value) {
        annotations.put(key, value);
    }

    public List<EnvVar> getEnvVars() {
        if (!env.isEmpty()) {
            return env
                .entrySet()
                .stream()
                .filter(e->StringUtils.isNotEmpty(e.getKey()))
                .filter(e->StringUtils.isNotEmpty(e.getValue()))
                .map(entry -> new EnvVar(entry.getKey(), entry.getValue(), null)).collect(Collectors.toList());
        }
        return Lists.newArrayList();
    }

    public void addLabels(Map<String, String> labels) {
        this.labels.putAll(labels);
    }

    public Map<String, String> getLabels() {
        return this.labels;
    }


    public ServiceVolumeComponent getServiceVolumeHolder() {
        return this.volumeComponent;
    }

    public RuntimeConfigSpec getConfiguration() {
        return configuration;
    }

    public String getName() {
        String name = "";
        if (configuration.getClass().isAssignableFrom(RuntimeConfigSpec.class)) {
            name = configuration.getName();
        } else {
            name = configuration.getName() == null ? configuration.getServiceName() : configuration.getName();
        }
        return name.toLowerCase();
    }

    public PodCreator createPod() {
        if (configuration.isBusiness()) {
            return new BusinessPodCreator(this);
        } else {
            return new SimplePodCreator(this);
        }
    }

    public void addVolume(ServiceVolumeSpec serviceVolumeSpec) {
        this.volumeComponent.addVolume(serviceVolumeSpec);
    }
}
