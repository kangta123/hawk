package com.oc.hawk.monitor.domain.measurement.unit;

/**
 * @author kangta123
 */
public class RateStorageMeasurementUnit extends StorageMeasurementUnit {

    public RateStorageMeasurementUnit(double v) {
        super(v);
    }

    @Override
    public String getDisplayName() {
        return unit + "/s";
    }
}
