package com.oc.hawk.monitor.port.driven.facade.prometheus;

/**
 * @author kangta123
 */
public interface PrometheusResultValueExtractor {
    String[][] extract(PrometheusQueryResult prometheusQueryResult);
}
