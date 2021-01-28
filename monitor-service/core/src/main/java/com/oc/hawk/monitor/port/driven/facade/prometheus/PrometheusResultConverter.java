package com.oc.hawk.monitor.port.driven.facade.prometheus;

/**
 * @author kangta123
 */
public interface PrometheusResultConverter {
    String[][] convert(String[][] value);
}
