package com.oc.hawk.container.domain.model.runtime.config;

import com.google.common.collect.Maps;
import com.oc.hawk.container.domain.model.runtime.SystemServicePort;
import com.oc.hawk.ddd.DomainValueObject;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@DomainValueObject
@Getter
@NoArgsConstructor
public class InstanceNetwork {
    private static final Integer DEFAULT_INNER_SERVER_PORT = 8080;

    private Map<Integer, Integer> customExposePorts;
    private Map<Integer, Integer> exposePorts;
    private Integer innerPort;
    private String serviceName;
    private boolean mesh;

    public InstanceNetwork(String serviceName, Integer innerPort, Boolean mesh, Map<Integer, Integer> exposePorts) {
        if (exposePorts != null) {
            exposePorts.forEach((k, v) -> {
                if (SystemServicePort.isSystemPort(k)) {
                    getNonNullExposePorts().put(k, v);
                } else {
                    getNonNullCustomExposePorts().put(k, v);
                }
            });
        }
        this.innerPort = innerPort;
        this.serviceName = serviceName;
        this.mesh = Objects.equals(Boolean.TRUE, mesh);
    }

    public static InstanceNetwork newInstanceEntryPoint(String serviceName, Boolean mesh, Integer innerPort, List<Integer> ports) {
        if (mesh == null) {
            mesh = true;
        }
        InstanceNetwork instanceNetwork = new InstanceNetwork();
        instanceNetwork.updateCustomExposePorts(ports);
        instanceNetwork.serviceName = serviceName;
        instanceNetwork.mesh = mesh;
        if (innerPort != null) {
            instanceNetwork.innerPort = innerPort;
        } else {
            instanceNetwork.innerPort = DEFAULT_INNER_SERVER_PORT;
        }

        return instanceNetwork;
    }

    public Map<Integer, Integer> getCustomExposePorts() {
        return customExposePorts;
    }

    public Map<Integer, Integer> getExposePorts() {
        return exposePorts;
    }


    public void updateInstanceEntryPont(Boolean mesh, List<Integer> exposePorts) {
        if (mesh != null) {
            this.mesh = mesh;
        }
        if (exposePorts != null) {
            updateCustomExposePorts(exposePorts);
        }
    }

    public Map<Integer, Integer> getAllExposePorts() {
        Map<Integer, Integer> all = Maps.newHashMap();
        if (customExposePorts != null) {
            all.putAll(customExposePorts);
        }
        if (exposePorts != null) {
            all.putAll(exposePorts);
        }
        return all;
    }

    public void exposePort(SystemServicePort port, Integer assignedPort) {
        if (exposePorts == null) {
            exposePorts = Maps.newHashMap();
        }
        assignedPort = assignedPort == null ? SystemServicePort.ANY_PORT.getPort() : assignedPort;
        if (exposePorts.containsKey(port.getPort())) {
            Integer val = exposePorts.get(port.getPort());
            if (SystemServicePort.isAnyPort(val)) {
                exposePorts.put(port.getPort(), assignedPort);
            }
        } else {
            exposePorts.put(port.getPort(), assignedPort);
        }
    }

    public void exposePort(SystemServicePort port) {
        exposePort(port, null);
    }

    public void updateExposedPort(Integer port, Integer assignedPort) {
        SystemServicePort systemServicePort = SystemServicePort.port(port);
        if (systemServicePort != null) {
            this.exposePort(systemServicePort, assignedPort);
        } else {
            getNonNullCustomExposePorts().put(port, assignedPort);
        }
    }

    private Map<Integer, Integer> getNonNullCustomExposePorts() {
        if (customExposePorts == null) {
            customExposePorts = Maps.newHashMap();
        }
        return customExposePorts;
    }

    private Map<Integer, Integer> getNonNullExposePorts() {
        if (exposePorts == null) {
            exposePorts = Maps.newHashMap();
        }
        return exposePorts;
    }

    private void updateCustomExposePorts(List<Integer> ports) {
        Map<Integer, Integer> customExposePorts = getNonNullCustomExposePorts();
        if (ports != null) {
            for (Integer port : ports) {
                customExposePorts.putIfAbsent(port, SystemServicePort.ANY_PORT.getPort());
            }
        }
    }

    public void discardPort(SystemServicePort port) {
        getNonNullExposePorts().remove(port.getPort());
    }
}
