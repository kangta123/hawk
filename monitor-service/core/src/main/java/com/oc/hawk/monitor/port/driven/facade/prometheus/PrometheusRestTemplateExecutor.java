package com.oc.hawk.monitor.port.driven.facade.prometheus;

import com.google.common.collect.Maps;
import com.oc.hawk.monitor.config.PrometheusConfig;
import com.oc.hawk.monitor.domain.service.FetchMeasurementsTemplate;
import com.oc.hawk.monitor.domain.service.IMeasureFetchExecutor;
import com.oc.hawk.monitor.domain.service.MeasurementTemplateQueryAssembler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;

/**
 * @author kangta123
 */
@Slf4j
@Component
public class PrometheusRestTemplateExecutor implements IMeasureFetchExecutor {
    private final RestTemplate restTemplate;

    private final PrometheusConfig config;
    private final MeasurementTemplateQueryAssembler queryAssembler;
    private final PrometheusResultValueExtractor extractor;

    public PrometheusRestTemplateExecutor(RestTemplate restTemplate, PrometheusConfig config) {
        this.restTemplate = restTemplate;
        this.config = config;
        queryAssembler = new PrometheusMeasurementTemplateQueryAssembler(config);
        extractor = new ValuePrecisionPrometheusResultValueExtractor();
    }

    @Override
    public String[][] fetch(FetchMeasurementsTemplate fetchMeasurementsTemplate) {
        Map<String, Object> params = Maps.newHashMap();
        final String query = queryAssembler.assemble(fetchMeasurementsTemplate);
        params.put("query", query);
        params.put("start", fetchMeasurementsTemplate.getStartUnixTimestamp());
        params.put("end", fetchMeasurementsTemplate.getEndUnixTimestamp());
        params.put("step", fetchMeasurementsTemplate.getDiffSecondBetweenStartAndEnd() / config.getMaxMeasurementCount());

        String queryStr = config.getHost() + "/api/v1/query_range?query={query}&start={start}&end={end}&step={step}";
        log.info("fetch measurement from prometheus with query {} ", query);

        ResponseEntity<PrometheusQueryResult> response = restTemplate.getForEntity(queryStr, PrometheusQueryResult.class, params);

        PrometheusQueryResult result = response.getBody();
        if (Objects.nonNull(result)) {
            if (result.isSuccess()) {
                return extractor.extract(result);
            } else {
                log.error("failed query data from prometheus with {}", queryStr);
                return null;
            }
        }
        return null;
    }


}
