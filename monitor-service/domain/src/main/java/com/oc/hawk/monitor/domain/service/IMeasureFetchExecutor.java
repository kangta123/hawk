package com.oc.hawk.monitor.domain.service;

import java.time.LocalDateTime;

/**
 * @author kangta123
 */
public interface IMeasureFetchExecutor {
    String[][] fetch(FetchMeasurementsTemplate fetchMeasurementsTemplate);
}
