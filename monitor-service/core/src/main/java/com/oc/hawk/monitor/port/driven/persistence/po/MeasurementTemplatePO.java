package com.oc.hawk.monitor.port.driven.persistence.po;

import com.oc.hawk.common.hibernate.BaseEntity;
import com.oc.hawk.monitor.domain.measurement.template.MeasurementScale;
import com.oc.hawk.monitor.domain.measurement.template.MeasurementTemplate;
import com.oc.hawk.monitor.domain.measurement.template.MeasurementTemplateID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Table(name = "monitor_measurement_template")
@Entity
@NoArgsConstructor
public class MeasurementTemplatePO extends BaseEntity {

    private String name;

    private String template;
    private String scale;
    private String title;
    private boolean enable;


    public MeasurementTemplate toMeasurementTemplate() {
        return MeasurementTemplate.builder()
            .id(new MeasurementTemplateID(getId()))
            .name(name)
            .scale(MeasurementScale.valueOf(scale))
            .title(title)
            .template(template)
            .enabled(enable)
            .build();
    }
}
