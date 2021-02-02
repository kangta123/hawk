package com.oc.hawk.kubernetes.keepalive.port.driving.reset;

import com.oc.hawk.kubernetes.keepalive.application.KubernetesProjectBuildLogUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/execution")
@RequiredArgsConstructor
@Slf4j
public class KubernetesExecutionController {

    private final KubernetesProjectBuildLogUseCase kubernetesProjectBuildLogUseCase;

    @PutMapping("/build/start")
    public void startExecution(@RequestParam String name,
                               @RequestParam String namespace,
                               @RequestParam String topic,
                               @RequestParam long domainId) {
        kubernetesProjectBuildLogUseCase.asyncWatchLog(namespace, name, domainId);
    }
}
