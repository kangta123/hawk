package com.oc.hawk.monitor.domain.service;

import com.oc.hawk.monitor.domain.measurement.Measurement;

import java.util.List;

/**
 * @author kangta123
 */
public interface IObtainMeasurement {
    List<Measurement> obtainMeasurements(FetchMeasurementsTemplate template);
}
