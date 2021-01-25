package com.oc.hawk.container.api.event;

import com.google.common.collect.ImmutableMap;
import lombok.Data;

import java.util.Map;

@Data
public class ProjectBuildRuntimeStopEvent {
    private String namespace;
    private Map<String, String> labels;

    public static ProjectBuildRuntimeStopEvent ofLabel(String namespace, String key, String value) {
        ProjectBuildRuntimeStopEvent projectBuildRuntimeStopEvent = new ProjectBuildRuntimeStopEvent();
        projectBuildRuntimeStopEvent.labels = ImmutableMap.of(key, value);
        projectBuildRuntimeStopEvent.namespace = namespace;
        return projectBuildRuntimeStopEvent;
    }

    public Map<String, String> getLabels() {
        return labels;
    }
}
