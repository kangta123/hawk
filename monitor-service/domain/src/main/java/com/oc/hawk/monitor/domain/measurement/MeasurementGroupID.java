package com.oc.hawk.monitor.domain.measurement;

import com.oc.hawk.ddd.DomainValueObject;
import lombok.Getter;

/**
 * @author kangta123
 */
@Getter
@DomainValueObject
public class MeasurementGroupID {
    private Long id;

    public MeasurementGroupID(Long id) {
        this.id = id;
    }
}
