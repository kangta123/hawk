package com.oc.hawk.monitor.domain.measurement.unit;

import com.oc.hawk.monitor.domain.MonitorDomainBaseTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author kangta123
 */
class TimeMeasurementUnitTest extends MonitorDomainBaseTest {

    @Test
    void getUnitValue_maxValueSmallerThenSecond() {
        final TimeMeasurementUnit timeMeasurementUnit = new TimeMeasurementUnit(0.00008);
        final double unitValue = timeMeasurementUnit.getUnitValue(0.00001);
        Assertions.assertThat(unitValue).isEqualTo(10);
    }
    @Test
    void getUnitValue_maxValueBiggerThenSecond() {
        final TimeMeasurementUnit timeMeasurementUnit = new TimeMeasurementUnit(7808);
        final double unitValue = timeMeasurementUnit.getUnitValue(100);
        Assertions.assertThat(unitValue).isEqualTo(100);
    }
}
