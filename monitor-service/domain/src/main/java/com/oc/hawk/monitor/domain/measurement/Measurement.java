package com.oc.hawk.monitor.domain.measurement;


import com.oc.hawk.monitor.domain.measurement.template.MeasurementTemplate;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 当获取到单个的数据点(如在一个模块的复审中发现的错误数)时，就建立了一个测量(measure)
 */
@Getter
public class Measurement {
    private MeasurementTemplate measurementTemplate;
    private Map<Long, String> data;

    public static Measurement create(MeasurementTemplate measurementTemplate, String[][] arr) {
        final Measurement measurement = new Measurement();
        measurement.measurementTemplate = measurementTemplate;
        Map<Long, String> map = new LinkedHashMap<>();
        if (arr != null) {
            for (String[] d : arr) {
                map.put(Long.parseLong(d[0]), d[1]);
            }
        }
        measurement.data = map;
        return measurement;
    }

}
