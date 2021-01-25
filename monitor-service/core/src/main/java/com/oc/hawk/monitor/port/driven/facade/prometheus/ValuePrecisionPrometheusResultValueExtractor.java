package com.oc.hawk.monitor.port.driven.facade.prometheus;

import com.oc.hawk.common.utils.DateUtils;

/**
 * @author kangta123
 */
public class ValuePrecisionPrometheusResultValueExtractor implements PrometheusResultValueExtractor {
    @Override
    public String[][] extract(PrometheusQueryResult prometheusQueryResult) {
        return fixValue(prometheusQueryResult);
    }

    private String[][] fixValue(PrometheusQueryResult prometheusQueryResult) {
        final String[][] values = prometheusQueryResult.getValues();
        if (values == null) {
            return null;
        }

        for (String[] str : values) {
            str[0] = formatDate(str[0]);
            str[1] = formatValue(str[1]);
        }
        return values;
    }

    private String formatDate(String value) {
        return String.valueOf(DateUtils.timestampUtcToCst(Long.parseLong(value) * 1000));
    }

    private String formatValue(String value) {
        final Double d = Double.valueOf(value);
        return String.format("%.2f", d);
    }
}
