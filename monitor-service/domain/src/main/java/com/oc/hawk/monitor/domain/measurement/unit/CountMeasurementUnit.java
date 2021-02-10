package com.oc.hawk.monitor.domain.measurement.unit;

/**
 * @author kangta123
 */
public class CountMeasurementUnit implements MeasurementUnit {
    private final double value;

    public CountMeasurementUnit(double value) {
        this.value = value;
    }

    @Override
    public String getDisplayName() {
        return "";
    }

    @Override
    public double getUnitValue(double value) {
        return value;
    }
}
