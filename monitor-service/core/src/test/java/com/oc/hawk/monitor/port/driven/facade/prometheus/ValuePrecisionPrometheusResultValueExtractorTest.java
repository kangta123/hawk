package com.oc.hawk.monitor.port.driven.facade.prometheus;

import com.oc.hawk.monitor.config.MonitorBaseTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @author kangta123
 */
class ValuePrecisionPrometheusResultValueExtractorTest extends MonitorBaseTest {

    @Test
    void extract() {
        final ValuePrecisionPrometheusResultValueExtractor extractor = new ValuePrecisionPrometheusResultValueExtractor();
        final PrometheusQueryResult prometheusQueryResult = newPrometheusQueryResult(new String[][]{new String[]{"1000", "100.23456"}, new String[]{"2000", "100"}, new String[]{"1000", "100.2"}});
        final String[][] extract = extractor.extract(prometheusQueryResult);

        Assertions.assertThat(extract).isEqualTo(new String[][]{new String[]{"1000", "100.23"}, new String[]{"2000", "100.00"}, new String[]{"1000", "100.20"}});
    }

    private PrometheusQueryResult newPrometheusQueryResult(String[][] values) {
        final PrometheusQueryResult prometheusQueryResult = new PrometheusQueryResult();
        prometheusQueryResult.setStatus(PrometheusStatus.success);
        final PrometheusData data = new PrometheusData();
        data.setResultType(PrometheusResultType.matrix);
        final PrometheusMetric metric = new PrometheusMetric();
        metric.setMetric(instance(PrometheusMetric.Metric.class));
        metric.setValues(values);
        data.setResult(List.of(metric));
        prometheusQueryResult.setData(data);
        return prometheusQueryResult;
    }
}
