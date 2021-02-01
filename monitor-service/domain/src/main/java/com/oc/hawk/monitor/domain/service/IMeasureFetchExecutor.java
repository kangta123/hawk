package com.oc.hawk.monitor.domain.service;

import java.time.LocalDateTime;

/**
 * 指标获取执行器
 *
 * @author kangta123
 */
public interface IMeasureFetchExecutor {
    String[][] fetch(FetchMeasurementsTemplate fetchMeasurementsTemplate);
}
