package com.oc.hawk.kubernetes;

import com.oc.hawk.kubernetes.runtime.port.driven.facade.BaseGateway;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public abstract class KubernetesBaseTest {
    @Mock
    protected BaseGateway baseGateway;

}
