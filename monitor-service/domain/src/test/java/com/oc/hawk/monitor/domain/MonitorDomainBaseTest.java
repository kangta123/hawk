package com.oc.hawk.monitor.domain;

import com.oc.hawk.monitor.domain.measurement.MeasurementGroupRepository;
import com.oc.hawk.monitor.domain.service.IMeasurementProvisioner;
import com.oc.hawk.test.BaseTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * @author kangta123
 */
@ExtendWith(MockitoExtension.class)
public class MonitorDomainBaseTest extends BaseTest {
    @Mock
    protected MeasurementGroupRepository measurementGroupRepository;

    @Mock
    protected IMeasurementProvisioner measurementFetcher;
}

