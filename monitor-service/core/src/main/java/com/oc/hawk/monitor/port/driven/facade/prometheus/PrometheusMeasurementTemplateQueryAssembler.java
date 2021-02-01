package com.oc.hawk.monitor.port.driven.facade.prometheus;

import com.google.common.collect.Maps;
import com.oc.hawk.monitor.config.PrometheusConfig;
import com.oc.hawk.monitor.domain.measurement.template.MeasurementTemplate;
import com.oc.hawk.monitor.domain.service.FetchMeasurementsTemplate;
import com.oc.hawk.monitor.domain.service.MeasurementTemplateQueryAssembler;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.C;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author kangta123
 */
@RequiredArgsConstructor
public class PrometheusMeasurementTemplateQueryAssembler implements MeasurementTemplateQueryAssembler {
    private final PrometheusConfig config;

    @Override
    public Map<String, Object> assemble(FetchMeasurementsTemplate fetchMeasurementsTemplate) {
        final long start = fetchMeasurementsTemplate.getStartUnixTimestamp();
        final long end = fetchMeasurementsTemplate.getEndUnixTimestamp();
        final int step = getStep(fetchMeasurementsTemplate);
        final String query = fetchMeasurementsTemplate.getTemplate().getTemplate().replace("$INTERVAL", Interval.getInterval(step) + "s").replace("$POD", fetchMeasurementsTemplate.getPod());

        return Map.of("query", query,
            "start", start,
            "end", end,
            "step", step);
    }

    private int getStep(FetchMeasurementsTemplate fetchMeasurementsTemplate) {
        return Math.toIntExact(fetchMeasurementsTemplate.getDiffSecondBetweenStartAndEnd() / config.getMaxMeasurementCount());
    }

    enum Interval {
        M1(60), M5(60 * 5), M15(60 * 15), H1(60 * 60), H2(60 * 60 * 2);
        private final int value;

        Interval(int value) {
            this.value = value;
        }


        static int getInterval(int step) {
            for (Interval v : Interval.values()) {
                if (v.value > step) {
                    return v.value;
                }
            }
            throw new RuntimeException("Do not support this time range");
        }

    }
}
