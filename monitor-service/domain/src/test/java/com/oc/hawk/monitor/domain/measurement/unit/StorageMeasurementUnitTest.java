package com.oc.hawk.monitor.domain.measurement.unit;

import com.oc.hawk.monitor.domain.MonitorDomainBaseTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author kangta123
 */
class StorageMeasurementUnitTest extends MonitorDomainBaseTest {

    @Test
    void getUnit_byteWhenMaxIsZero() {
        final StorageMeasurementUnit storageMeasurementUnit = new StorageMeasurementUnit(0);
        Assertions.assertThat(storageMeasurementUnit.getUnit()).isEqualTo(Unit.BYTE);
    }

    @Test
    void getUnitValue_valueIsZero() {
        final StorageMeasurementUnit storageMeasurementUnit = new StorageMeasurementUnit(0);
        final double unitValue = storageMeasurementUnit.getUnitValue(0);
        Assertions.assertThat(unitValue).isEqualTo(0d);
    }
}
