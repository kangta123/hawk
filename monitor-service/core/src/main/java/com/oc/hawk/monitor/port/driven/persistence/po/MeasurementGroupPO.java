package com.oc.hawk.monitor.port.driven.persistence.po;

import com.google.common.collect.Lists;
import com.oc.hawk.api.utils.JsonUtils;
import com.oc.hawk.common.hibernate.BaseEntity;
import com.oc.hawk.monitor.domain.measurement.MeasurementGroup;
import com.oc.hawk.monitor.domain.measurement.MeasurementGroupID;
import com.oc.hawk.monitor.domain.measurement.MeasurementGroupName;
import com.oc.hawk.monitor.domain.measurement.MeasurementUnit;
import com.oc.hawk.monitor.domain.measurement.template.MeasurementTemplate;
import com.oc.hawk.monitor.domain.measurement.template.MeasurementTemplateID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Table(name = "monitor_measurement_group")
@Entity
@NoArgsConstructor
public class MeasurementGroupPO extends BaseEntity {

    private String name;

    private String measurements;
    private String unit;
    private boolean enable;
    private String title;

    public MeasurementGroup toMeasurementGroup(List<MeasurementTemplate> templates) {
        return new MeasurementGroup(new MeasurementGroupID(getId()), new MeasurementGroupName(name), templates, new MeasurementUnit(getUnit()), isEnable(), title);
    }
}
