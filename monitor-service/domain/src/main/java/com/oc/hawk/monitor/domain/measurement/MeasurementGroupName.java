package com.oc.hawk.monitor.domain.measurement;

import com.oc.hawk.ddd.DomainValueObject;
import lombok.Getter;

/**
 * @author kangta123
 */
@DomainValueObject
@Getter
public class MeasurementGroupName {
    private final String name;

    public MeasurementGroupName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
