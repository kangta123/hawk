package com.oc.hawk.monitor.port.driven.facade.prometheus;

import com.oc.hawk.monitor.domain.measurement.Measurement;
import com.oc.hawk.monitor.domain.service.FetchMeasurementsTemplate;
import com.oc.hawk.monitor.domain.service.IMeasureFetchExecutor;
import com.oc.hawk.monitor.domain.service.IMeasurementProvisioner;
import org.springframework.stereotype.Component;

/**
 * 从prometheus抓取指标
 *
 * @author kangta123
 */
@Component
public class PrometheusMeasurementProvisioner implements IMeasurementProvisioner {
    private final IMeasureFetchExecutor measurementFetchExecutor;

    public PrometheusMeasurementProvisioner(IMeasureFetchExecutor measurementFetchExecutor) {
        this.measurementFetchExecutor = measurementFetchExecutor;
    }

    @Override
    public Measurement fetchMeasurement(FetchMeasurementsTemplate fetchMeasurementsTemplate) {
        final String[][] data = measurementFetchExecutor.fetch(fetchMeasurementsTemplate);
        return Measurement.create(fetchMeasurementsTemplate.getTemplate(), data);
    }

}
