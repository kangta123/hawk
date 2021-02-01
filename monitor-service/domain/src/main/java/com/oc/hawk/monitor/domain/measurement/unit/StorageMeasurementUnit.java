package com.oc.hawk.monitor.domain.measurement.unit;

/**
 * @author kangta123
 */
public class StorageMeasurementUnit implements MeasurementUnit {
    final Unit unit;

    public StorageMeasurementUnit(double size) {
        this.unit = calculateSuitableUnit(size);
    }

    private Unit calculateSuitableUnit(double size) {
        for (Unit unit : Unit.values()) {
            if (size >= unit.getUnitSize()) {
                return unit;
            }
        }
        return null;
    }

    @Override
    public String getDisplayName() {
        return String.valueOf(unit);
    }

    @Override
    public double getUnitValue(double value) {
        if (value == 0 || unit.getUnitSize() == 0) {
            return value;
        }
        return value / Long.valueOf(unit.getUnitSize()).doubleValue();
    }

    public Unit getUnit() {
        return unit;
    }
}
