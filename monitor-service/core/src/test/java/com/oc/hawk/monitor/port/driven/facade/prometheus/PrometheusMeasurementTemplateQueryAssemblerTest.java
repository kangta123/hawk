package com.oc.hawk.monitor.port.driven.facade.prometheus;

import com.oc.hawk.monitor.config.MonitorBaseTest;
import com.oc.hawk.monitor.config.PrometheusConfig;
import com.oc.hawk.monitor.domain.measurement.MeasurementGroupName;
import com.oc.hawk.monitor.domain.measurement.template.MeasurementTemplate;
import com.oc.hawk.monitor.domain.service.FetchMeasurementsTemplate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author kangta123
 */
class PrometheusMeasurementTemplateQueryAssemblerTest extends MonitorBaseTest {
    private PrometheusConfig config = new PrometheusConfig("http://localhost", 100);

    @Test
    void assemble_podPlaceHolderIsReplaced() {
        final PrometheusMeasurementTemplateQueryAssembler prometheusMeasurementTemplateQueryAssembler = new PrometheusMeasurementTemplateQueryAssembler(config);

        final String pod = anyStr();
        final MeasurementTemplate t = MeasurementTemplate.builder().template("123$PODabc$POD$").build();
        final FetchMeasurementsTemplate template = new FetchMeasurementsTemplate(instance(MeasurementGroupName.class), pod, anyDateTime(), anyDateTime(), t);
        final String assemble = prometheusMeasurementTemplateQueryAssembler.assemble(template);


        Assertions.assertThat(assemble).contains("123" + pod + "abc" + pod + "$");
    }
}
