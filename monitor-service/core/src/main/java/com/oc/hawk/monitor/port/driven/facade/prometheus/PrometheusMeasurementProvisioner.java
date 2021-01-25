package com.oc.hawk.monitor.port.driven.facade.prometheus;

import com.oc.hawk.monitor.domain.measurement.Measurement;
import com.oc.hawk.monitor.domain.service.FetchMeasurementsTemplate;
import com.oc.hawk.monitor.domain.service.IMeasureFetchExecutor;
import com.oc.hawk.monitor.domain.service.IMeasurementProvisioner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author kangta123
 */
@RequiredArgsConstructor
@Component
public class PrometheusMeasurementProvisioner implements IMeasurementProvisioner {
    private final IMeasureFetchExecutor measurementFetchExecutor;
    private PrometheusResultValueExtractor extractor = new ValuePrecisionPrometheusResultValueExtractor();

    @Override
    public Measurement fetchMeasurement(FetchMeasurementsTemplate fetchMeasurementsTemplate) {

        final String[][] data = measurementFetchExecutor.fetch(fetchMeasurementsTemplate);

        return Measurement.create(fetchMeasurementsTemplate.getTemplate(), data);
    }

}
