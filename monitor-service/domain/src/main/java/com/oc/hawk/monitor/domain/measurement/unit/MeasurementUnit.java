package com.oc.hawk.monitor.domain.measurement.unit;

/**
 * @author kangta123
 */
public interface MeasurementUnit {
    String getDisplayName();

    double getUnitValue(double value);
}
