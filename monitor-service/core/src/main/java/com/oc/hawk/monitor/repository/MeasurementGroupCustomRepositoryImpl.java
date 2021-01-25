package com.oc.hawk.monitor.repository;

import com.google.common.collect.Lists;
import com.oc.hawk.common.spring.ApplicationContextHolder;
import com.oc.hawk.monitor.domain.measurement.MeasurementGroupRepository;
import com.oc.hawk.monitor.port.driven.persistence.po.MeasurementTemplatePO;
import com.oc.hawk.monitor.port.driven.persistence.po.MeasurementGroupPO;
import com.oc.hawk.monitor.application.QueryMetricGroupParam;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.Predicate;
import java.util.List;
import java.util.stream.Collectors;

public class MeasurementGroupCustomRepositoryImpl  {
    public List<MeasurementGroupPO> find(QueryMetricGroupParam param) {
//        Specification<MeasurementGroupPO> specification = createSpecification(param);
//        MeasurementGroupRepository repository = ApplicationContextHolder.getBean(MeasurementGroupRepository.class);
//        List<MeasurementGroupPO> measurementGroupEntities = repository.findAll(specification);
////        measurementGroupEntities.forEach(m -> {
////            m.setMeasurement(m.getMeasurement().stream().filter(MeasurementTemplatePO::isEnable).collect(Collectors.toList()));
////        });
//        return measurementGroupEntities;
        return null;
    }

    private Specification<MeasurementGroupPO> createSpecification(QueryMetricGroupParam param) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = Lists.newArrayList();
            if (!CollectionUtils.isEmpty(param.getGroupIds())) {
                predicates.add(root.get("id").in(param.getGroupIds()));
            }
            if (!CollectionUtils.isEmpty(param.getGroupNames())) {
                predicates.add(root.get("name").in(param.getGroupNames()));
            }
            if (!CollectionUtils.isEmpty(param.getMetrics())) {
                predicates.add(root.get("metric").in(param.getMetrics()));
            }
            predicates.add(criteriaBuilder.isTrue(root.get("enable")));
            if (predicates.isEmpty()) {
                return null;
            } else {
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }
        };
    }
}
