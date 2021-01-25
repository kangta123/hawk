package com.oc.hawk.monitor.domain.metric;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TypedMetric extends Metric {
    private Object type;

    public TypedMetric(String time, String metric, Object type) {
        super(time, metric);
        this.type = type;
    }
}
