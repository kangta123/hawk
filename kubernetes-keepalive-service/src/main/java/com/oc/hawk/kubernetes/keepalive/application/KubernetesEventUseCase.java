package com.oc.hawk.kubernetes.keepalive.application;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.oc.hawk.api.constant.KafkaTopic;
import com.oc.hawk.container.api.event.ContainerDomainEventType;
import com.oc.hawk.ddd.event.DomainEvent;
import com.oc.hawk.ddd.event.EventPublisher;
import com.oc.hawk.infrastructure.application.KubernetesEventApi;
import com.oc.hawk.infrastructure.application.KubernetesPod;
import com.oc.hawk.infrastructure.application.representation.KubernetesPodRepresentation;
import com.oc.hawk.kubernetes.api.constants.RuntimeInfoDTO;
import com.oc.hawk.kubernetes.domain.model.KubernetesLabel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@RequiredArgsConstructor
public class KubernetesEventUseCase {
    private final KubernetesEventApi kubernetesEventApi;

    private final KubernetesEventRepository kubernetesEventRepository;
    private final KubernetesPodRepresentation kubernetesPodRepresentation;
    private final EventPublisher eventPublisher;
    private final ExecutorService executorService = new ThreadPoolExecutor(
        1, 1, 0, TimeUnit.SECONDS,
        new ArrayBlockingQueue<>(10),
        new ThreadFactoryBuilder().setDaemon(true).setNameFormat("HAWK-kubernetes-watch-%d").build()
    );

    @PostConstruct
    public void init() {
        startWatching();
    }

    public void startWatching() {
        String resourceVersion = kubernetesEventRepository.loadEventResourceVersion();
        executorService.execute(() -> watch(resourceVersion));
    }

    private void watch(String resourceVersion) {
        kubernetesEventApi.watch(
            pod -> {
                KubernetesPod kubernetesRuntime = KubernetesPod.createNew(pod);
                if (!kubernetesRuntime.isBusinessRuntime()) {
                    log.debug("Ignore null projectId pod {} event ", kubernetesRuntime.getRuntimeId());
                    return;
                }

                RuntimeInfoDTO runtimeInfoDTO = kubernetesPodRepresentation.toRuntimeInfoDTO(kubernetesRuntime);
                eventPublisher.publishEvent(KafkaTopic.INFRASTRUCTURE_RESOURCE_TOPIC, DomainEvent.byData(ContainerDomainEventType.RUNTIME_STATE_UPDATED, runtimeInfoDTO));
                log.info("Kubernetes event received {}-{} with version {}", kubernetesRuntime.getRuntimeId(), kubernetesRuntime.getRuntimeStatus(), kubernetesRuntime.getResourceVersion());

                kubernetesEventRepository.updateEventResourceVersion(kubernetesRuntime.getResourceVersion());
            },
            KubernetesLabel.withBusinessService(), resourceVersion);
    }

}
