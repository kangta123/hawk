package com.oc.hawk.monitor.application.representation;

import com.google.common.collect.Lists;
import com.oc.hawk.monitor.domain.measurement.Measurement;
import com.oc.hawk.monitor.domain.measurement.MeasurementGroup;
import com.oc.hawk.monitor.domain.measurement.MeasurementGroupName;
import com.oc.hawk.monitor.domain.measurement.MeasurementGroupRepository;
import com.oc.hawk.monitor.domain.measurement.template.MeasurementTemplate;
import com.oc.hawk.monitor.dto.MeasurementDTO;
import com.oc.hawk.monitor.dto.MeasurementGroupDTO;
import com.oc.hawk.monitor.dto.MeasurementPointDTO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author kangta123
 */
@Component
@RequiredArgsConstructor
public class MeasurementRepresentation {
    private final MeasurementGroupRepository measurementGroupRepository;

    public MeasurementGroupDTO toMeasurementGroupDTO(MeasurementGroupName name, List<Measurement> measurements) {
        final MeasurementGroup measurementGroup = measurementGroupRepository.byName(name);

        final MeasurementGroupDTO queryMeasurementResultDTO = new MeasurementGroupDTO();
        queryMeasurementResultDTO.setName(name.getName());
        queryMeasurementResultDTO.setTitle(measurementGroup.getTitle());
        queryMeasurementResultDTO.setUnit(measurementGroup.getUnit().getUnit());

        if (!CollectionUtils.isEmpty(measurements)) {
            queryMeasurementResultDTO.setMeasurements(
                measurements.stream().map(m -> {
                    MeasurementDTO dto = new MeasurementDTO();
                    final List<MeasurementPointDTO> data = dto.getData();
                    m.getData().forEach((d, v) -> {
                        data.add(new MeasurementPointDTO(d, v));
                    });

                    final MeasurementTemplate measurementTemplate = m.getMeasurementTemplate();
                    dto.setName(measurementTemplate.getName());
                    dto.setUnit(measurementTemplate.getUnit());
                    dto.setTitle(measurementTemplate.getTitle());
                    return dto;
                }).collect(Collectors.toList()));
        } else {
            queryMeasurementResultDTO.setMeasurements(Lists.newArrayList());
        }
        return queryMeasurementResultDTO;
    }
}
