package com.oc.hawk.monitor.domain.metric;


import com.oc.hawk.monitor.domain.util.MetricUnitUtils;

public enum MetricUnitType {
    Byte, Percent, Flow, TimeMS, COUNT;

    public String format(String value) {
        switch (this) {
            case Flow:
                return String.valueOf(MetricUnitUtils.toInt(value) / 1024);
            case Byte:
                return String.valueOf(MetricUnitUtils.toInt(value) / 1024 / 1024);
            case Percent:
                return String.valueOf(MetricUnitUtils.round(Double.parseDouble(value) * 100, 2));
            default:
                return value;
        }
    }
}
