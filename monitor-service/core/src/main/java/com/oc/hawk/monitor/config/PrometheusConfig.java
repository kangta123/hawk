package com.oc.hawk.monitor.config;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author kangta123
 */
@Data
@NoArgsConstructor
public class PrometheusConfig {
    private String host;
    private int maxMeasurementCount;

    public PrometheusConfig(String host, int maxMeasurementCount) {
        this.host = host;
        this.maxMeasurementCount = maxMeasurementCount;
    }
}
