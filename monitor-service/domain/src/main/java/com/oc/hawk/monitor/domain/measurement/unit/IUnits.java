package com.oc.hawk.monitor.domain.measurement.unit;

/**
 * @author kangta123
 */
public interface IUnits {
    String format(long size, String pattern);

    long getUnitSize();
}

