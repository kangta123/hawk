package com.oc.hawk.monitor.domain.service;

import com.oc.hawk.monitor.domain.MonitorDomainBaseTest;
import com.oc.hawk.monitor.domain.measurement.Measurement;
import com.oc.hawk.monitor.domain.measurement.MeasurementGroup;
import com.oc.hawk.monitor.domain.measurement.MeasurementGroupID;
import com.oc.hawk.monitor.domain.measurement.template.MeasurementTemplate;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.when;

/**
 * @author kangta123
 */
public class MeasurementGroupObtainMeasurementTest extends MonitorDomainBaseTest {

    @Test
    public void testObtainMeasurements_returnEmptyIfGroupNameIsNull() {
        final FetchMeasurementsTemplate template = template();
        setObjectValue(template, "group", null);

        final List<Measurement> measurements = new MeasurementGroupObtainMeasurement(measurementGroupRepository, measurementFetcher).obtainMeasurements(template);

        Assertions.assertThat(measurements).isEmpty();
    }

    @Test
    public void testObtainMeasurements_returnEmptyIfGroupIsNull() {
        final FetchMeasurementsTemplate template = template();
        when(measurementGroupRepository.byName(template.getName())).thenReturn(null);
        final List<Measurement> measurements = new MeasurementGroupObtainMeasurement(measurementGroupRepository, measurementFetcher).obtainMeasurements(template);

        Assertions.assertThat(measurements).isEmpty();
    }

    private FetchMeasurementsTemplate template() {
        return instance(FetchMeasurementsTemplate.class);
    }

    @Test
    public void testObtainMeasurements_returnEmptyIfTemplatesIsEmpty() {
        final FetchMeasurementsTemplate template = template();
        final MeasurementGroup measurementGroup = new MeasurementGroup(new MeasurementGroupID(along()), template.getName(), Lists.newArrayList(), true, str());
        when(measurementGroupRepository.byName(template.getName())).thenReturn(measurementGroup);

        final List<Measurement> measurements = new MeasurementGroupObtainMeasurement(measurementGroupRepository, measurementFetcher).obtainMeasurements(template);

        Assertions.assertThat(measurements).isEmpty();
    }

    @Test
    public void testObtainMeasurements_returnEmptyIfGroupDisabled() {
        final FetchMeasurementsTemplate template = template();
        final MeasurementGroup measurementGroup = new MeasurementGroup(new MeasurementGroupID(along()), template.getName(), Lists.newArrayList(instance(MeasurementTemplate.class)), false, str());

        when(measurementGroupRepository.byName(template.getName())).thenReturn(measurementGroup);

        final List<Measurement> measurements = new MeasurementGroupObtainMeasurement(measurementGroupRepository, measurementFetcher).obtainMeasurements(template);

        Assertions.assertThat(measurements).isEmpty();
    }
}
