package com.oc.hawk.monitor.domain.service;

import com.oc.hawk.monitor.domain.measurement.Measurement;

/**
 * 指标提供者
 * @author kangta123
 */
public interface IMeasurementProvisioner {
    Measurement fetchMeasurement(FetchMeasurementsTemplate fetchMeasurementsTemplate);
}
