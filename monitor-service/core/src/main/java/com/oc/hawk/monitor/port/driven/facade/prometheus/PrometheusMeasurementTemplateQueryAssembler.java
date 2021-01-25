package com.oc.hawk.monitor.port.driven.facade.prometheus;

import com.oc.hawk.monitor.config.PrometheusConfig;
import com.oc.hawk.monitor.domain.measurement.template.MeasurementTemplate;
import com.oc.hawk.monitor.domain.service.FetchMeasurementsTemplate;
import com.oc.hawk.monitor.domain.service.MeasurementTemplateQueryAssembler;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.C;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author kangta123
 */
@RequiredArgsConstructor
public class PrometheusMeasurementTemplateQueryAssembler implements MeasurementTemplateQueryAssembler {
    private final PrometheusConfig config;

    @Override
    public String assemble(FetchMeasurementsTemplate template) {
        final String measurementTemplate = template.getTemplate().getTemplate().replace("$POD", template.getPod());

        return measurementTemplate;
    }
}
