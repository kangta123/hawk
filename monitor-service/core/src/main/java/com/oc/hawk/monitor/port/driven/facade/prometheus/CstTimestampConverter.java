package com.oc.hawk.monitor.port.driven.facade.prometheus;

import com.oc.hawk.common.utils.DateUtils;

/**
 * @author kangta123
 */
public class CstTimestampConverter implements PrometheusResultConverter {
    @Override
    public String[][] convert(String[][] value) {
        return fixValue(value);
    }

    private String[][] fixValue(String[][] values) {
        if (values != null) {
            for (String[] str : values) {
                str[0] = formatDate(str[0]);
            }
        }
        return values;
    }

    private String formatDate(String value) {
        final double v = Double.parseDouble(value);
        return String.valueOf(DateUtils.timestampUtcToCst((long) v));
    }

}
