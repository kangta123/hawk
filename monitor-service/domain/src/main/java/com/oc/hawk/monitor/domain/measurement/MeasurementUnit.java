package com.oc.hawk.monitor.domain.measurement;

import com.oc.hawk.ddd.DomainValueObject;
import com.oc.hawk.monitor.domain.util.StorageUnits;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author kangta123
 */
@DomainValueObject
@Getter
public class MeasurementUnit {
    private final String unit;

    public MeasurementUnit(String unit) {
        this.unit = unit;
    }


}
