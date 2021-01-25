package com.oc.hawk.monitor.port.driven.persistence.po;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oc.hawk.common.hibernate.BaseEntity;
import com.oc.hawk.monitor.domain.measurement.template.MeasurementTemplate;
import com.oc.hawk.monitor.domain.measurement.template.MeasurementTemplateID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Table(name = "monitor_measurement_template")
@Entity
@NoArgsConstructor
public class MeasurementTemplatePO extends BaseEntity {

    private String name;

    private String template;
    private String unit;
    private String title;
    private boolean enable;


    public MeasurementTemplate toMeasurementTemplate() {
        return MeasurementTemplate.builder()
            .id(new MeasurementTemplateID(getId()))
            .name(name)
            .unit(unit)
            .title(title)
            .template(template)
            .enabled(enable)
            .build();
    }
}
