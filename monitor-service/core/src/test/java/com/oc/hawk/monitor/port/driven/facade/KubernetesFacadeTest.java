package com.oc.hawk.monitor.port.driven.facade;

import com.google.common.collect.Lists;
import com.oc.hawk.kubernetes.api.constants.RuntimeInfoDTO;
import com.oc.hawk.monitor.config.MonitorBaseTest;
import com.oc.hawk.monitor.port.driven.facade.feign.KubernetesGateway;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.rmi.Remote;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
        final RuntimeInfoDTO inst = instance(RuntimeInfoDTO.class);
        setObjectValue(inst, "startTime", 1000L);
        final RuntimeInfoDTO inst1 = instance(RuntimeInfoDTO.class);
        setObjectValue(inst1, "startTime", 2000L);
        final RuntimeInfoDTO inst2 = instance(RuntimeInfoDTO.class);
        setObjectValue(inst2, "startTime", 3000L);
        when(kubernetesGateway.getAvailableRuntimeInfo(any(), any(), any(), any(), true)).thenReturn(Lists.newArrayList(inst1, inst2, inst));

        final String availablePodName = kubernetesFacade.getAvailablePodName(anyStr(), anyStr(), anyStr(), any());

        Assertions.assertThat(availablePodName).isEqualTo(inst2.getPodName());
    }

    @Test
    public void testGetAvailablePodName_withoutAvailablePod() {
        final KubernetesFacade kubernetesFacade = new RemoteKubernetesFacade(kubernetesGateway);
        when(kubernetesGateway.getAvailableRuntimeInfo(any(), any(), any(), any(), true)).thenReturn(Lists.newArrayList());

        final String availablePodName = kubernetesFacade.getAvailablePodName(anyStr(), anyStr(), anyStr(), any());

        Assertions.assertThat(availablePodName).isEmpty();
    }

    @Test
    public void testGetAvailablePodName_withSingleAvailablePod() {
        final KubernetesFacade kubernetesFacade = new RemoteKubernetesFacade(kubernetesGateway);
        final RuntimeInfoDTO instance = instance(RuntimeInfoDTO.class);
        when(kubernetesGateway.getAvailableRuntimeInfo(any(), any(), any(), any(), true)).thenReturn(List.of(instance));

        final String availablePodName = kubernetesFacade.getAvailablePodName(anyStr(), anyStr(), anyStr(), any());

        Assertions.assertThat(availablePodName).isEqualTo(instance.getPodName());
    }
}
