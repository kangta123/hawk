package com.oc.hawk.monitor.domain.metric;

import com.google.common.base.Objects;
import lombok.Data;

@Data
public class MetricData extends Metric implements Comparable <MetricData> {

    private MetricUnitType unitType;
    private MetricType type;
    private MetricGroup group;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MetricData that = (MetricData) o;
        return Objects.equal(getLabel(), that.getLabel()) &&
                type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getLabel(), type);
    }

    @Override
    public int compareTo(MetricData o) {
        return Math.toIntExact(Long.parseLong(this.getLabel()) - Long.parseLong(o.getLabel()));
    }

}
