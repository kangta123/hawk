package com.oc.hawk.monitor.port.driven.facade.prometheus;

import com.oc.hawk.monitor.config.MonitorBaseTest;
import com.oc.hawk.monitor.config.PrometheusConfig;
import com.oc.hawk.monitor.domain.measurement.MeasurementGroupName;
import com.oc.hawk.monitor.domain.measurement.template.MeasurementTemplate;
import com.oc.hawk.monitor.domain.service.FetchMeasurementsTemplate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

/**
 * @author kangta123
 */
class PrometheusMeasurementTemplateQueryAssemblerTest extends MonitorBaseTest {
    private final PrometheusConfig config = new PrometheusConfig("http://localhost", 100);

    @Test
    void assemble_podPlaceHolderIsReplaced() {
        final PrometheusMeasurementTemplateQueryAssembler prometheusMeasurementTemplateQueryAssembler = new PrometheusMeasurementTemplateQueryAssembler(config);

        final String pod = str();
        final LocalDateTime now = LocalDateTime.now();
        final FetchMeasurementsTemplate template = FetchMeasurementsTemplate.builder()
            .start(now.minusHours(1))
            .end(now)
            .template(MeasurementTemplate.builder().template("123$PODabc$POD$").build())
            .pod(pod)
            .name(newInstance(MeasurementGroupName.class))
            .build();
        final String assemble = prometheusMeasurementTemplateQueryAssembler.assemble(template).get("query").toString();

        Assertions.assertThat(assemble).contains("123" + pod + "abc" + pod + "$");
    }
}
