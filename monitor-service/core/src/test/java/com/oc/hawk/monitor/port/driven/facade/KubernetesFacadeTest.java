package com.oc.hawk.monitor.port.driven.facade;

import com.google.common.collect.Lists;
import com.oc.hawk.kubernetes.api.constants.RuntimeInfoDTO;
import com.oc.hawk.monitor.config.MonitorBaseTest;
import com.oc.hawk.monitor.port.driven.facade.feign.KubernetesGateway;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

/**
 * @author kangta123
 */
public class KubernetesFacadeTest extends MonitorBaseTest {
    @Mock
    private KubernetesGateway kubernetesGateway;

    @Test
    public void testGetAvailablePodName_getLatestPodNameWithMultiAvailablePod() {
        final KubernetesFacade kubernetesFacade = new RemoteKubernetesFacade(kubernetesGateway);
        final RuntimeInfoDTO inst = newInstance(RuntimeInfoDTO.class);
        setObjectValue(inst, "startTime", 1000L);
        final RuntimeInfoDTO inst1 = newInstance(RuntimeInfoDTO.class);
        setObjectValue(inst1, "startTime", 2000L);
        final RuntimeInfoDTO inst2 = newInstance(RuntimeInfoDTO.class);
        setObjectValue(inst2, "startTime", 3000L);
        when(kubernetesGateway.getAvailableRuntimeInfo(any(), any(), any(), any(), any())).thenReturn(Lists.newArrayList(inst1, inst2, inst));

        final String availablePodName = kubernetesFacade.getAvailablePodName(str(), str(), str(), str());

        Assertions.assertThat(availablePodName).isEqualTo(inst2.getPodName());
    }

    @Test
    public void testGetAvailablePodName_withoutAvailablePod() {
        final KubernetesFacade kubernetesFacade = new RemoteKubernetesFacade(kubernetesGateway);
        when(kubernetesGateway.getAvailableRuntimeInfo(any(), any(), any(), any(), any())).thenReturn(Lists.newArrayList());

        final String availablePodName = kubernetesFacade.getAvailablePodName(str(), str(), str(), str());

        Assertions.assertThat(availablePodName).isEmpty();
    }

    @Test
    public void testGetAvailablePodName_withSingleAvailablePod() {
        final KubernetesFacade kubernetesFacade = new RemoteKubernetesFacade(kubernetesGateway);
        final RuntimeInfoDTO instance = newInstance(RuntimeInfoDTO.class);
        when(kubernetesGateway.getAvailableRuntimeInfo(anyString(), anyString(), anyString(), anyString(), anyBoolean())).thenReturn(List.of(instance));

        final String availablePodName = kubernetesFacade.getAvailablePodName(str(), str(), str(), str());

        Assertions.assertThat(availablePodName).isEqualTo(instance.getPodName());
    }
}
