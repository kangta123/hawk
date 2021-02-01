package com.oc.hawk.monitor.domain.service;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.oc.hawk.ddd.DomainService;
import com.oc.hawk.monitor.domain.measurement.MeasurementGroupName;
import com.oc.hawk.monitor.domain.measurement.Measurement;
import com.oc.hawk.monitor.domain.measurement.MeasurementGroup;
import com.oc.hawk.monitor.domain.measurement.MeasurementGroupRepository;
import com.oc.hawk.monitor.domain.measurement.template.MeasurementTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 通过指标组获取指标数据
 * @author kangta123
 */
@DomainService
@Slf4j
public class MeasurementGroupObtainMeasurement implements IObtainMeasurement {
    private static ThreadPoolExecutor metricExtractThreadPool;
    private final MeasurementGroupRepository measurementGroupRepository;
    private final IMeasurementProvisioner measurementProvisioner;

    static {
        ThreadFactory factory = new ThreadFactoryBuilder().setDaemon(true).setNameFormat("HawkMeasurementGroup-Extractor-%d").build();
        metricExtractThreadPool = new ThreadPoolExecutor(10, 100, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10), factory);
    }

    public MeasurementGroupObtainMeasurement(MeasurementGroupRepository measurementGroupRepository, IMeasurementProvisioner measurementProvisioner) {
        this.measurementGroupRepository = measurementGroupRepository;
        this.measurementProvisioner = measurementProvisioner;
    }

    @Override
    public List<Measurement> obtainMeasurements(FetchMeasurementsTemplate fetchMeasurementsTemplate) {
        final MeasurementGroupName name = fetchMeasurementsTemplate.getName();
        if (name == null) {
            return Lists.newArrayList();
        }
        final MeasurementGroup measurementGroup = measurementGroupRepository.byName(name);
        if (measurementGroup == null) {
            log.warn("no such measurement group {} ", name);
            return Lists.newArrayList();
        }
        if (!measurementGroup.isEnable()) {
            log.warn("measurement group {} disabled ", measurementGroup.getName());
            return Lists.newArrayList();
        }
        final List<MeasurementTemplate> enabledTemplates = measurementGroup.getEnabledTemplates();
        if (CollectionUtils.isEmpty(enabledTemplates)) {
            log.warn("no measurement templates in group {} ", measurementGroup.getName());
            return Lists.newArrayList();
        }
        return doObtain(enabledTemplates, fetchMeasurementsTemplate);
    }

    private List<Measurement> doObtain(List<MeasurementTemplate> templates, FetchMeasurementsTemplate fetchMeasurementsTemplate) {
        Stopwatch started = Stopwatch.createStarted();
        List<Measurement> result = Lists.newArrayListWithCapacity(templates.size());

        templates.stream().map(template ->
            metricExtractThreadPool.submit(() ->
                measurementProvisioner.fetchMeasurement(fetchMeasurementsTemplate.withMeasurementTemplate(template))))
            .forEach(f -> {
                try {
                    result.add(f.get());
                } catch (Exception e) {
                    log.error("fetch measurement error", e);
                }
            });

        log.info("obtain metric takes {}ms", started.elapsed(TimeUnit.MILLISECONDS));
        return result;
    }
}
