package com.oc.hawk.monitor.domain.measurement;

import com.oc.hawk.ddd.DomainValueObject;
import com.oc.hawk.monitor.domain.measurement.template.MeasurementScale;
import com.oc.hawk.monitor.domain.measurement.template.MeasurementTemplate;
import com.oc.hawk.monitor.domain.measurement.unit.MeasurementUnit;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Slf4j
@DomainValueObject
public class MeasurementGroup {
    private final MeasurementGroupName name;
    private final List<MeasurementTemplate> templates;
    private final boolean enable;
    private final MeasurementGroupID id;
    private final String title;

    public MeasurementGroup(MeasurementGroupID id, MeasurementGroupName name, List<MeasurementTemplate> templates, boolean enable, String title) {
        this.id = id;
        this.name = name;
        this.templates = templates;
        this.enable = enable;
        this.title = title;
    }

    public List<MeasurementTemplate> getEnabledTemplates() {
        if (templates != null) {
            return templates.stream().filter(MeasurementTemplate::isEnabled).collect(Collectors.toList());
        }
        return null;
    }


    public MeasurementUnit getSuitableUnit(List<Measurement> measurements) {
        final MeasurementScale scale = getScale();
        double max = 0;
        for (Measurement measurement : measurements) {
            double val = measurement.getMaxValue();
            if (max < val) {
                max = val;
            }
        }

        final MeasurementUnit unit = scale.getUnit(max);
        if (unit == null) {
            throw new RuntimeException("Measurement unit cannot be null");
        }
        return unit;
    }

    private MeasurementScale getScale() {
        MeasurementScale scale = null;
        for (MeasurementTemplate template : templates) {
            if (scale == null) {
                scale = template.getScale();
            }
            if (scale != template.getScale()) {
                throw new IllegalArgumentException("there must be the same scale in measurement group");
            }
        }
        return scale;
    }
}
