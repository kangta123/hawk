package com.oc.hawk.monitor.port.driven.facade.prometheus;

import com.oc.hawk.monitor.config.MonitorBaseTest;
import com.oc.hawk.monitor.config.PrometheusConfig;
import com.oc.hawk.monitor.domain.measurement.MeasurementGroupName;
import com.oc.hawk.monitor.domain.measurement.template.MeasurementTemplate;
import com.oc.hawk.monitor.domain.service.FetchMeasurementsTemplate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author kangta123
 */
class PrometheusMeasurementTemplateQueryAssemblerTest extends MonitorBaseTest {
    private PrometheusConfig config = new PrometheusConfig("http://localhost", 100);

    @Test
    void assemble_podPlaceHolderIsReplaced() {
        final PrometheusMeasurementTemplateQueryAssembler prometheusMeasurementTemplateQueryAssembler = new PrometheusMeasurementTemplateQueryAssembler(config);

        final String pod = str();
        final FetchMeasurementsTemplate template = FetchMeasurementsTemplate.builder()
            .start(anyDateTime())
            .end(anyDateTime())
            .template(MeasurementTemplate.builder().template("123$PODabc$POD$").build())
            .pod(pod)
            .name(instance(MeasurementGroupName.class))
            .build();
        final String assemble = prometheusMeasurementTemplateQueryAssembler.assemble(template);


        Assertions.assertThat(assemble).contains("123" + pod + "abc" + pod + "$");
    }
}
