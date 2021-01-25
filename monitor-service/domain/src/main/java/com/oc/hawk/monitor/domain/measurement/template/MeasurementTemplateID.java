package com.oc.hawk.monitor.domain.measurement.template;

import com.oc.hawk.ddd.DomainValueObject;
import lombok.Getter;

/**
 * @author kangta123
 */
@DomainValueObject
@Getter
public class MeasurementTemplateID {
    private Long id;

    public MeasurementTemplateID(Long id) {
        this.id = id;
    }
}
