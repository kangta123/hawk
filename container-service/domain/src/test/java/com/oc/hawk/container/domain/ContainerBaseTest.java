package com.oc.hawk.container.domain;

import com.oc.hawk.container.domain.model.runtime.config.InstanceConfigRepository;
import com.oc.hawk.test.BaseTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ContainerBaseTest extends BaseTest {
    @Mock
    protected InstanceConfigRepository instanceConfigRepository;
}
