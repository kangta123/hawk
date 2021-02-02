package com.oc.hawk.kubernetes.keepalive.port.driving.reset;

import com.oc.hawk.kubernetes.api.dto.KubernetesLogDTO;
import com.oc.hawk.kubernetes.keepalive.application.KubernetesContainerLogUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/service/log")
@RequiredArgsConstructor
public class KubernetesLogController {
    private final KubernetesContainerLogUseCase kubernetesContainerLogUseCase;

    @GetMapping("/{name}")
    public KubernetesLogDTO getLogService(
        @PathVariable("name") String name,
        @RequestParam(value = "podIndex", required = false, defaultValue = "0") int podIndex,
        @RequestParam(required = false, defaultValue = "default") String namespace,
        @RequestParam(value = "timestamp", required = false) Integer timestamp) {
        return kubernetesContainerLogUseCase.readFullLogs(name, namespace, podIndex, timestamp);
    }

}
