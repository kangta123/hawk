package com.oc.hawk.monitor.port.driven.persistence;

import com.google.common.collect.Lists;
import com.oc.hawk.api.utils.JsonUtils;
import com.oc.hawk.monitor.domain.measurement.MeasurementGroup;
import com.oc.hawk.monitor.domain.measurement.MeasurementGroupName;
import com.oc.hawk.monitor.domain.measurement.MeasurementGroupRepository;
import com.oc.hawk.monitor.domain.measurement.template.MeasurementTemplate;
import com.oc.hawk.monitor.port.driven.persistence.po.MeasurementGroupPO;
import com.oc.hawk.monitor.port.driven.persistence.po.MeasurementTemplatePO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
interface MeasurementGroupPoRepository extends JpaRepositoryImplementation<MeasurementGroupPO, Long> {
    MeasurementGroupPO findByName(String name);
}

@Repository
interface MeasurementTemplatePoRepository extends JpaRepositoryImplementation<MeasurementTemplatePO, Long> {
    List<MeasurementTemplatePO> findByIdIn(List<Long> ids);
}

/**
 * @author kangta123
 */
@Component
@RequiredArgsConstructor
public class JpaMeasurementGroupRepository implements MeasurementGroupRepository {
    private final MeasurementGroupPoRepository measurementGroupPoRepository;
    private final MeasurementTemplatePoRepository measurementTemplatePoRepository;

    @Override
    public MeasurementGroup byName(MeasurementGroupName name) {
        final MeasurementGroupPO group = measurementGroupPoRepository.findByName(name.getName());
        if (group == null) {
            return null;
        }
        List<MeasurementTemplate> templates = Lists.newArrayList();
        if (StringUtils.isNotEmpty(group.getMeasurements())) {
            final List<Long> ids = JsonUtils.json2List(group.getMeasurements(), Long.class);
            templates = measurementTemplatePoRepository.findByIdIn(ids).stream().map(MeasurementTemplatePO::toMeasurementTemplate).collect(Collectors.toList());
        }
        return group.toMeasurementGroup(templates);
    }
}
