package com.oc.hawk.monitor.port.driven.persistence.po;

import com.oc.hawk.common.hibernate.BaseEntity;
import com.oc.hawk.monitor.domain.measurement.MeasurementGroup;
import com.oc.hawk.monitor.domain.measurement.MeasurementGroupID;
import com.oc.hawk.monitor.domain.measurement.MeasurementGroupName;
import com.oc.hawk.monitor.domain.measurement.template.MeasurementTemplate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Getter
@Setter
@Table(name = "monitor_measurement_group")
@Entity
@NoArgsConstructor
public class MeasurementGroupPO extends BaseEntity {

    private String name;

    private String measurements;
    private boolean enable;
    private String title;

    public MeasurementGroup toMeasurementGroup(List<MeasurementTemplate> templates) {
        return new MeasurementGroup(new MeasurementGroupID(getId()), new MeasurementGroupName(name), templates, isEnable(), title);
    }
}
