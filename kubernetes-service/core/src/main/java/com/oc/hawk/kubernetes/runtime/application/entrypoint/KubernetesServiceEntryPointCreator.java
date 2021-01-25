package com.oc.hawk.kubernetes.runtime.application.entrypoint;

import com.oc.hawk.container.api.event.EntryPointUpdatedEvent;
import com.oc.hawk.infrastructure.application.KubernetesApi;
import com.oc.hawk.kubernetes.domain.model.IServiceLabelConstants;
import com.oc.hawk.kubernetes.runtime.application.runtime.spec.NetworkConfigSpec;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServiceBuilder;
import io.fabric8.kubernetes.api.model.ServicePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class KubernetesServiceEntryPointCreator implements ServiceEntryPointCreator {
    private final KubernetesApi kubernetesApi;

    @Override
    public EntryPointUpdatedEvent createServiceEntryPoint(String namespace, Long projectId, NetworkConfigSpec networkConfigSpec) {
        Service service = getService(namespace, networkConfigSpec);

        service = kubernetesApi.createService(namespace, service);
        log.info("Create entry point {} in {} namespace", networkConfigSpec.getServiceName(), namespace);

        if (service != null && networkConfigSpec.getNetworkType() == ClusterIPType.NodePort) {
            Map<Integer, Integer> ipPorts = service.getSpec().getPorts().stream()
                .collect(Collectors.toMap(sp -> sp.getTargetPort().getIntVal(), ServicePort::getNodePort));
            return new EntryPointUpdatedEvent(namespace, networkConfigSpec.getServiceName(), ipPorts, projectId);
        }
        return null;

    }

    private Service getService(String namespace, NetworkConfigSpec configuration) {
        String serviceName = configuration.getServiceName();
        ClusterIPType networkType = configuration.getNetworkType();

        return new ServiceBuilder()
            .withNewMetadata()
            .withNamespace(namespace)
            .withName(serviceName)
            .endMetadata()
            .withNewSpec()
            .withSelector(Collections.singletonMap(IServiceLabelConstants.LABEL_VERSION, serviceName))
            .addToPorts(configuration.getServicePorts())
            .withType(networkType.name())
            .endSpec()
            .withNewStatus()
            .endStatus()
            .build();
    }

}
