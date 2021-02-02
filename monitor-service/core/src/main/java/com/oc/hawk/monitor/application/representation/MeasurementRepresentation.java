package com.oc.hawk.monitor.application.representation;

import com.google.common.collect.Lists;
import com.oc.hawk.monitor.domain.measurement.Measurement;
import com.oc.hawk.monitor.domain.measurement.MeasurementGroup;
import com.oc.hawk.monitor.domain.measurement.MeasurementGroupName;
import com.oc.hawk.monitor.domain.measurement.MeasurementGroupRepository;
import com.oc.hawk.monitor.domain.measurement.template.MeasurementTemplate;
import com.oc.hawk.monitor.domain.measurement.unit.MeasurementUnit;
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

        final MeasurementUnit unit = measurementGroup.getSuitableUnit(measurements);
        if (!CollectionUtils.isEmpty(measurements)) {
            queryMeasurementResultDTO.setMeasurements(
                measurements.stream().map(m -> {
                    final MeasurementTemplate measurementTemplate = m.getMeasurementTemplate();
                    MeasurementDTO dto = new MeasurementDTO();

                    final List<MeasurementPointDTO> data = m.unitedData(unit).entrySet()
                        .stream()
                        .map((e) -> new MeasurementPointDTO(e.getKey(), e.getValue()))
                        .collect(Collectors.toList());
                    dto.setData(data);

                    dto.setName(measurementTemplate.getName());
                    dto.setUnit(unit.getDisplayName());
                    dto.setTitle(measurementTemplate.getTitle());
                    return dto;
                }).collect(Collectors.toList()));
            queryMeasurementResultDTO.setUnit(unit.getDisplayName());
        } else {
            queryMeasurementResultDTO.setMeasurements(Lists.newArrayList());
        }
        return queryMeasurementResultDTO;
    }

    private String formatValue(String value) {
        final Double d = Double.valueOf(value);
        return String.format("%.2f", d);
    }
}
