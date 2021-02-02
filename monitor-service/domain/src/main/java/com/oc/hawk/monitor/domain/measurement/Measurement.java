package com.oc.hawk.monitor.domain.measurement;


import com.oc.hawk.monitor.domain.measurement.template.MeasurementTemplate;
import com.oc.hawk.monitor.domain.measurement.unit.MeasurementUnit;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 当获取到单个的数据点(如在一个模块的复审中发现的错误数)时，就建立了一个测量(measure)
 */
public class Measurement {
    private MeasurementTemplate measurementTemplate;
    private Map<Long, Double> data;

    public static Measurement create(MeasurementTemplate measurementTemplate, String[][] arr) {
        final Measurement measurement = new Measurement();
        measurement.measurementTemplate = measurementTemplate;
        Map<Long, Double> map = new LinkedHashMap<>();
        if (arr != null) {
            for (String[] d : arr) {
                map.put(Long.parseLong(d[0]), Double.parseDouble(d[1]));
            }
        }
        measurement.data = map;
        return measurement;
    }

    public double getMaxValue() {
        if (data == null) {
            return 0f;
        }
        return data.values().stream().max(Double::compare).orElse(0D);
    }

    public Map<Long, Double> unitedData(MeasurementUnit unit) {
        for (Long d : data.keySet()) {
            final Double value = data.get(d);
            data.put(d, unit.getUnitValue(value));
        }
        return data;
    }

    public MeasurementTemplate getMeasurementTemplate() {
        return measurementTemplate;
    }
}
