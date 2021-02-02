package com.oc.hawk.monitor.domain.measurement.unit;

/**
 * @author kangta123
 */
public class PercentMeasurementUnit implements MeasurementUnit {
    @Override
    public String getDisplayName() {
        return "%";
    }

    @Override
    public double getUnitValue(double value) {
        return value;
    }
}
