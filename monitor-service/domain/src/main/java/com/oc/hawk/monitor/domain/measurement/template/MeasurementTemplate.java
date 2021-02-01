package com.oc.hawk.monitor.domain.measurement.template;

import com.oc.hawk.ddd.DomainEntity;
import lombok.Builder;
import lombok.Getter;

/**
 * @author kangta123
 */
@DomainEntity
@Getter
@Builder
public class MeasurementTemplate {
    private final MeasurementTemplateID id;
    private final String template;
    private final String name;
    private final MeasurementScale scale;
    private final boolean enabled;
    private final String title;
}
