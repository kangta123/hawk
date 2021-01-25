package com.oc.hawk.kubernetes.runtime.application.runtime;

import com.oc.hawk.container.api.event.ProjectBuildRuntimeStopEvent;
import com.oc.hawk.infrastructure.application.KubernetesApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KubernetesProjectBuildExecutorUseCase {
    private final KubernetesApi kubernetesApi;


    public void stop(ProjectBuildRuntimeStopEvent eventData) {
        log.info("Stop Normal runtime {} in {}", eventData.getLabels().values(), eventData.getNamespace());
        kubernetesApi.deletePod(eventData.getNamespace(), eventData.getLabels());
    }
}
