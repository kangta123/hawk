package com.oc.hawk.monitor.domain.measurement;

import com.oc.hawk.monitor.domain.MonitorDomainBaseTest;
import com.oc.hawk.monitor.domain.measurement.template.MeasurementScale;
import com.oc.hawk.monitor.domain.measurement.template.MeasurementTemplate;
import com.oc.hawk.monitor.domain.measurement.unit.StorageMeasurementUnit;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

/**
 * @author kangta123
 */
class MeasurementTest extends MonitorDomainBaseTest {

    @Test
    void testGetMaxValue_maxValue() {
        final Measurement measurement = generateMeasurementOfStorage();
        final double maxValue = measurement.getMaxValue();

        Assertions.assertThat(maxValue).isEqualTo(1024);
    }

    @Test
    void testGetMaxValue_withNullValues() {
        final Measurement measurement = new Measurement();
        final double maxValue = measurement.getMaxValue();

        Assertions.assertThat(maxValue).isEqualTo(0f);
    }

    private Measurement generateMeasurementOfStorage() {
        final MeasurementTemplate template = newInstance(MeasurementTemplate.class);
        setObjectValue(template, "scale", MeasurementScale.storage);
        return Measurement.create(template, new String[][]{
            new String[]{"1000", "102.4"},
            new String[]{"2000", "1024"},
            new String[]{"3000", "512"}
        });
    }

    @Test
    void testUnitedData_convertToKB() {
        final Measurement measurement = generateMeasurementOfStorage();
        final int kb = 1024 * 100;
        final Map<Long, Double> result = measurement.unitedData(new StorageMeasurementUnit(kb));

        Assertions.assertThat(result.values()).contains(0.1, 1.0, 0.5);

    }
}
