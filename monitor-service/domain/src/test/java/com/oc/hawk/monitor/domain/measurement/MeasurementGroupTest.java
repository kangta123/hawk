package com.oc.hawk.monitor.domain.measurement;

import com.oc.hawk.monitor.domain.MonitorDomainBaseTest;
import com.oc.hawk.monitor.domain.measurement.template.MeasurementTemplate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @author kangta123
 */
class MeasurementGroupTest extends MonitorDomainBaseTest {

    @Test
    void getEnabledTemplates_withOutEnabledTemplates() {
        final MeasurementGroup instance = newInstance(MeasurementGroup.class, "templates", List.of(template(false), template(false)));
        Assertions.assertThat(instance.getEnabledTemplates()).isEmpty();
    }

    @Test
    void getEnabledTemplates_withOneEnabledTemplates() {
        final MeasurementGroup instance = newInstance(MeasurementGroup.class, "templates", List.of(template(false), template(true), template(false)));
        Assertions.assertThat(instance.getEnabledTemplates()).hasSize(1);
    }

    private MeasurementTemplate template(boolean enabled) {
        return newInstance(MeasurementTemplate.class, "enabled", enabled);
    }
}
