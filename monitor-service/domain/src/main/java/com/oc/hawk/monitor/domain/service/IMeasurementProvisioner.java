package com.oc.hawk.monitor.domain.service;

import com.oc.hawk.monitor.domain.measurement.Measurement;

/**
 * @author kangta123
 */
public interface IMeasurementProvisioner {
    Measurement fetchMeasurement(FetchMeasurementsTemplate fetchMeasurementsTemplate);
}
