package com.oc.hawk.monitor.port.driven.facade.prometheus;

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

    public PrometheusRestTemplateExecutor(RestTemplate restTemplate, PrometheusConfig config) {
        this.restTemplate = restTemplate;
        this.config = config;
        queryAssembler = new PrometheusMeasurementTemplateQueryAssembler(config);
    }

    @Override
    public String[][] fetch(FetchMeasurementsTemplate fetchMeasurementsTemplate) {


        final Map<String, Object> assemble = queryAssembler.assemble(fetchMeasurementsTemplate);


        String queryStr = config.getHost() + "/api/v1/query_range?query={query}&start={start}&end={end}&step={step}";
        log.info("fetch measurement from prometheus with param {}", assemble);

        ResponseEntity<PrometheusQueryResult> response = restTemplate.getForEntity(queryStr, PrometheusQueryResult.class, assemble);

        PrometheusQueryResult result = response.getBody();
        if (Objects.nonNull(result)) {
            if (result.isSuccess()) {
                return result.getValues();
            } else {
                log.error("failed query data from prometheus with {}", queryStr);
                return null;
            }
        }
        return null;
    }


}
