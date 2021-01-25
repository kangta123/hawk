package com.oc.hawk.monitor.domain.metric;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Metric {
    private String label;
    private String metric;

    public Metric(String label, String metric) {
        this.label = label;
        this.metric = metric;
    }
}
