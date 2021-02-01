package com.oc.hawk.monitor.domain.measurement;

import com.oc.hawk.monitor.domain.MonitorDomainBaseTest;
import com.oc.hawk.monitor.domain.measurement.unit.MeasurementUnit;
import com.oc.hawk.monitor.domain.measurement.unit.StorageMeasurementUnit;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author kangta123
 */
class StorageMeasurementScaleTest extends MonitorDomainBaseTest {

    @Test
    void testApply_suitableValueWithStorageUnit() {
        final double v = 1024 * 1024 * 5.123d;
        final MeasurementUnit unit = new StorageMeasurementUnit(v);
        final double unitValue = unit.getUnitValue(v);
        Assertions.assertThat(unitValue).isEqualTo(5.123);

    }
}
