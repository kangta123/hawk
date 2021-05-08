package com.oc.hawk.infrastructure.application;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.Watcher;
import io.fabric8.kubernetes.client.WatcherException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
@Slf4j
public class KubernetesEventApi {
    public static final String OLD_VERSION_ERROR_MESSAGE = "too old resource version";
    private final KubernetesClient client;

    public void watch(Consumer<Pod> eventConsumer, Map<String, String> labels, String resourceVersion) {
        if (eventConsumer == null) {
            throw new IllegalArgumentException("Event consumer cannot be null");
        }
        log.info("Kubernetes event watch start with version {}", resourceVersion);

        client.pods().inAnyNamespace().withLabels(labels).withResourceVersion(resourceVersion).watch(new Watcher<>() {
            @Override
            public void eventReceived(Watcher.Action action, Pod pod) {
                KubernetesPod kubernetesRuntime = KubernetesPod.createNew(pod);
                switch (action.name()) {
                    case "MODIFIED":
                        if (kubernetesRuntime.ignoreUpdateEvent()) {
                            return;
                        }
                        eventConsumer.accept(pod);
                        break;
                    case "DELETED":
                    case "ADDED":
                        eventConsumer.accept(pod);
                        break;
                    default:
                        log.error("Unrecognized event: {}", action.name());
                }

            }

            @Override
            public void onClose() {
                log.info("Watch gracefully closed");
            }

            @Override
            public void onClose(WatcherException e) {
                log.error("Kubernetes Pod Event watcher is down, ", e);
                if (e.getMessage().contains(OLD_VERSION_ERROR_MESSAGE)) {
                    watch(eventConsumer, labels, null);
                }
            }

        });

    }


}
