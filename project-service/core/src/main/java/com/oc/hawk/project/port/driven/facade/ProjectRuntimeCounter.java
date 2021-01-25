package com.oc.hawk.project.port.driven.facade;

import lombok.Data;

@Data
public class ProjectRuntimeCounter {
    private Integer runningCount;
    private Integer configCount;
    public ProjectRuntimeCounter(Integer configCount, Integer runningCount) {
        this.configCount = configCount;
        this.runningCount = runningCount;
    }
}
