package com.oc.hawk.container.api.event;

import lombok.Data;

import java.util.Map;

@Data
public class ProjectBuildRuntimeStopEvent {
    private String namespace;
    private Map<String, String> labels;

    public static ProjectBuildRuntimeStopEvent ofLabel(String namespace, Map<String, String> labels) {
        ProjectBuildRuntimeStopEvent projectBuildRuntimeStopEvent = new ProjectBuildRuntimeStopEvent();
        projectBuildRuntimeStopEvent.labels = labels;
        projectBuildRuntimeStopEvent.namespace = namespace;
        return projectBuildRuntimeStopEvent;
    }

    public Map<String, String> getLabels() {
        return labels;
    }
}
