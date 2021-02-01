package com.oc.hawk.monitor.domain.measurement.unit;


import java.util.concurrent.TimeUnit;

/**
 * @author kangta123
 */
public class TimeMeasurementUnit implements MeasurementUnit {
    private TimeUnit unit;

    public TimeMeasurementUnit(double max) {
        calculate(max);
    }

    @Override
    public String getDisplayName() {
        return unit.display;
    }

    @Override
    public double getUnitValue(double value) {
        return unit.scale * value;
    }


    private void calculate(double value) {
        if (value > 0) {
            unit = TimeUnit.getTimeUnit(value);
        } else {
            unit = TimeUnit.SECONDS;
        }
    }

    private enum TimeUnit {
        SECONDS("s", 1), MILLISECONDS("ms", 1000), MICROSECONDS("μs", 1000 * 1000), NANOSECONDS("ns", 1000 * 1000 * 1000);
        private final String display;
        private final long scale;

        TimeUnit(String display, long scale) {
            this.display = display;
            this.scale = scale;
        }

        static TimeUnit getTimeUnit(double value) {
            for (TimeUnit u : TimeUnit.values()) {
                if (value * u.scale > 1) {
                    return u;
                }
            }
            return null;
        }
    }

}

