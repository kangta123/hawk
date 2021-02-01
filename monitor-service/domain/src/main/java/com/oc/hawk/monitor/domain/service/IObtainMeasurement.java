package com.oc.hawk.monitor.domain.service;

import com.oc.hawk.monitor.domain.measurement.Measurement;

import java.util.List;

/**
 * 获取指标数据
 *
 * @author kangta123
 */
public interface IObtainMeasurement {
    List<Measurement> obtainMeasurements(FetchMeasurementsTemplate template);
}
