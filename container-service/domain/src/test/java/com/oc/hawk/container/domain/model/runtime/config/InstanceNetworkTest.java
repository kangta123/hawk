package com.oc.hawk.container.domain.model.runtime.config;

import com.google.common.collect.Maps;
import com.oc.hawk.container.domain.ContainerBaseTest;
import com.oc.hawk.container.domain.model.runtime.SystemServicePort;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

class InstanceNetworkTest extends ContainerBaseTest {


    @Test
    void exposePort() {
        int assignPort = integer();
        Map<Integer, Integer> ports = Maps.newHashMap();
        SystemServicePort sshPort = SystemServicePort.SSH_PORT;
        ports.put(sshPort.getPort(), assignPort);
        InstanceNetwork instanceNetwork = new InstanceNetwork(str(), integer(), true, ports);
        instanceNetwork.exposePort(sshPort);

        Assertions.assertThat(instanceNetwork.getAllExposePorts().get(sshPort.getPort())).isEqualTo(assignPort);
    }
}
