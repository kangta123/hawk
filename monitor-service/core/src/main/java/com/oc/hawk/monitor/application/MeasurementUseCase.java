package com.oc.hawk.monitor.application;

import com.oc.hawk.monitor.application.representation.MeasurementRepresentation;
import com.oc.hawk.monitor.domain.measurement.Measurement;
import com.oc.hawk.monitor.domain.measurement.MeasurementGroupName;
import com.oc.hawk.monitor.domain.measurement.MeasurementGroupRepository;
import com.oc.hawk.monitor.domain.service.FetchMeasurementsTemplate;
import com.oc.hawk.monitor.domain.service.IMeasurementProvisioner;
import com.oc.hawk.monitor.domain.service.IObtainMeasurement;
import com.oc.hawk.monitor.domain.service.MeasurementGroupObtainMeasurement;
import com.oc.hawk.monitor.dto.MeasurementGroupDTO;
import com.oc.hawk.monitor.dto.QueryMeasurementDTO;
import com.oc.hawk.monitor.port.driven.facade.KubernetesFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class MeasurementUseCase {

    private final MeasurementGroupRepository measurementGroupRepository;
    private final IMeasurementProvisioner measurementFetcher;
    private final MeasurementRepresentation measurementRepresentation;
    private final KubernetesFacade kubernetesFacade;
    private IObtainMeasurement obtainMeasurement;

    @PostConstruct
    public void setup() {
        obtainMeasurement = new MeasurementGroupObtainMeasurement(measurementGroupRepository, measurementFetcher);
    }

    public MeasurementGroupDTO measure(QueryMeasurementDTO queryMeasurementDTO) {
        if (queryMeasurementDTO.getStart() == null || queryMeasurementDTO.getEnd() == null) {
            throw new IllegalArgumentException("start & end time cannot be null");
        }
        final MeasurementGroupName name = new MeasurementGroupName(queryMeasurementDTO.getGroupName());
        String pod = kubernetesFacade.getAvailablePodName(queryMeasurementDTO.getNamespace(), queryMeasurementDTO.getApp(), queryMeasurementDTO.getVersion(), queryMeasurementDTO.getName());
        if (StringUtils.isEmpty(pod)) {
            log.warn("Available pod  not found with name {} - {}", queryMeasurementDTO.getName(), queryMeasurementDTO.getApp());
            return null;
        }

        final FetchMeasurementsTemplate template = FetchMeasurementsTemplate.builder()
            .name(name)
            .pod(pod)
            .start(queryMeasurementDTO.getStart())
            .end(queryMeasurementDTO.getEnd())
            .build();
        final List<Measurement> measurements = obtainMeasurement.obtainMeasurements(template);

        return measurementRepresentation.toMeasurementGroupDTO(name, measurements);
//        List<MeasurementGroupEntity> measurementGroupEntities = queryMeasurementGroup(
//            queryServiceMetricDTO.createQueryMeasurementGroup());
//        MeasurementExtractInfo info = getMeasurementExtractInfo(queryServiceMetricDTO);
//
//        return measurementGroupEntities.stream().map(mg -> {
//            MeasurementGroup measurementGroup = BeanUtils.transform(MeasurementGroup.class, mg);
//            measurementGroup.setInfo(info);
//            measurementGroup.setMetricAggregate(MetricAssemblerMethod.valueOf(mg.getMetricAggregateType()));
//            List<Measurement> measurements = mg.getMeasurement().stream().map(m->{
//                Measurement measurement = BeanUtils.transform(Measurement.class, m);
//                measurement.setGroup(measurementGroup);
//                return measurement;
//            }).collect(Collectors.toList());
//            measurementGroup.setMeasurements(measurements);
//            return measurementGroup;
//        }).map(m -> m.extract(serviceMetricExtractor)).collect(Collectors.toList());
    }
//
//    private MeasurementExtractInfo getMeasurementExtractInfo(QueryMetricDTO queryServiceMetricDTO) {
//        MeasurementExtractInfo info = new MeasurementExtractInfo();
//        info.setEnd(queryServiceMetricDTO.getEnd());
//        info.setStart(queryServiceMetricDTO.getStart());
//        info.setApp(queryServiceMetricDTO.getApp());
//        info.setVersion(queryServiceMetricDTO.getVersion());
//        info.setNamespace(StringUtils.defaultString(queryServiceMetricDTO.getNamespace()));
//        return info;
//    }
//
//    private List<MeasurementGroupEntity> queryMeasurementGroup(QueryMetricDTO param) {
//        QueryMetricGroupParam queryMetricGroupParam = BeanUtils.transform(QueryMetricGroupParam.class, param);
//        return measurementGroupRepository.find(queryMetricGroupParam);
//    }

}
