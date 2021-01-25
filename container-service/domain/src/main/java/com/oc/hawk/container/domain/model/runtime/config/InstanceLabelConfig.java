package com.oc.hawk.container.domain.model.runtime.config;

import com.google.common.collect.Maps;
import com.oc.hawk.ddd.DomainValueObject;

import java.util.Map;

@DomainValueObject
public class InstanceLabelConfig {
    private final static String LABEL_NAMESPACE = "service.namespace";
    private final static String LABEL_TAG = "service.tag";

    private final Map<String, String> labels = Maps.newHashMap();
    private InstanceConfig instanceConfig;


    public InstanceLabelConfig(InstanceConfig instanceConfig) {
        this.instanceConfig = instanceConfig;
    }

    public InstanceLabelConfig withNamespace(String namespace) {
        labels.put(LABEL_NAMESPACE, namespace);
        return this;
    }

    public Map<String, String> labels() {
        return this.labels;
    }

    public InstanceLabelConfig withTag(String tag) {
        labels.put(LABEL_TAG, tag);
        return this;
    }

    public InstanceLabelConfig withRequiredLabels() {
        BaseInstanceConfig baseConfig = (BaseInstanceConfig) instanceConfig.getBaseConfig();
        InstanceImage image = baseConfig.getImage();
        return withNamespace(baseConfig.getNamespace()).withTag(image.getTag());
    }
}
