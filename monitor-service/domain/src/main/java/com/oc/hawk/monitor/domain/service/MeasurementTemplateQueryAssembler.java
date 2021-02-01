package com.oc.hawk.monitor.domain.service;

import java.util.Map;

/**
 * 组装查询语句模版
 *
 * @author kangta123
 */
public interface MeasurementTemplateQueryAssembler {
    Map<String, Object> assemble(FetchMeasurementsTemplate template);
}
