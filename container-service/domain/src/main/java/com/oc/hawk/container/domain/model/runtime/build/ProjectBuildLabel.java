package com.oc.hawk.container.domain.model.runtime.build;

import com.oc.hawk.container.domain.model.runtime.info.RuntimeCatalog;
import lombok.Getter;

import java.util.Map;

/**
 * @author kangta123
 */
@Getter
public enum ProjectBuildLabel {

    BUILD_JOB_ID("build_job"),
    RUNTIME_CATALOG("runtime_catalog");
    private final String label;

    ProjectBuildLabel(String label) {
        this.label = label;
    }

    public static Map<String, String> buildLabels(long buildJobId) {
        return Map.of(RUNTIME_CATALOG.label, RuntimeCatalog.BUILD.toString(), BUILD_JOB_ID.label, String.valueOf(buildJobId));
    }


    public static Map<String, String> stopBuildLabels(Long domainId) {
        return Map.of(BUILD_JOB_ID.label, String.valueOf(domainId));
    }
}
