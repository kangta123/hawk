package com.oc.hawk.monitor.domain.measurement;

/**
 * @author kangta123
 */
public interface MeasurementGroupRepository {
    MeasurementGroup byName(MeasurementGroupName name);
}
