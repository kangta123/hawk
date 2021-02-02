package com.oc.hawk.monitor.domain.service;

/**
 * 指标获取执行器
 *
 * @author kangta123
 */
public interface IMeasureFetchExecutor {
    String[][] fetch(FetchMeasurementsTemplate fetchMeasurementsTemplate);
}
