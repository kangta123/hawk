package com.oc.hawk.kubernetes.domain.service;

import com.oc.hawk.kubernetes.domain.model.RuntimeRestartCountExceededException;

public class RuntimeInspectionService {
    public final int MAX_RESTART_COUNT = 3;

    public void checkRuntimeStatus(int restartCount) {
        if (restartCount > MAX_RESTART_COUNT) {
            throw new RuntimeRestartCountExceededException();
        }
    }
}
