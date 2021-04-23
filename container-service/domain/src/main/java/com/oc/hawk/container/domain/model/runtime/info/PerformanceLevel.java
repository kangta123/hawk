package com.oc.hawk.container.domain.model.runtime.info;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public enum PerformanceLevel {
    NORMAL(0, 1, 1.5, 1.5),
    MEDIUM(0, 2, 4, 4),
    LARGE(0, 4, 8, 8),
    UNLIMITED(-1, -1, -1, -1);

    private final String resCpu;
    private final String limitCpu;
    private final String resMem;
    private final String limitMem;

    PerformanceLevel(double resCpu, double limitCpu, double resMem, double limitMem) {
        this.resCpu = String.valueOf(resCpu);
        this.limitCpu = String.valueOf(limitCpu);
        this.resMem = resMem + "Gi";
        this.limitMem = limitMem + "Gi";
    }

    public static PerformanceLevel getWithDefaultPerformanceLevel(String performance) {
        if (StringUtils.isEmpty(performance)) {
            return MEDIUM;
        } else {
            return PerformanceLevel.valueOf(performance);
        }
    }

    public boolean isUnlimited() {
        return this == UNLIMITED;
    }
}
