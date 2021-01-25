package com.oc.hawk.kubernetes.runtime.port.driving.reset;

import com.oc.hawk.common.spring.mvc.BooleanWrapper;
import com.oc.hawk.kubernetes.api.constants.RuntimeInfoDTO;
import com.oc.hawk.kubernetes.runtime.application.runtime.KubernetesRuntimeUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Slf4j
public class KubernetesRuntimeController {

    private final KubernetesRuntimeUseCase kubernetesRuntimeUseCase;

    @GetMapping("/ns/runtime")
    public List<RuntimeInfoDTO> queryRuntimeInfo(@RequestParam(required = false, defaultValue = "default") String namespace,
                                                 @RequestParam(required = false) Long projectId,
                                                 @RequestParam(required = false) String name,
                                                 @RequestParam(required = false) String version,
                                                 @RequestParam(required = false) String app,
                                                 @RequestParam(required = false, defaultValue = "false") boolean ready) {
        return kubernetesRuntimeUseCase.queryRuntimeInfo(namespace, projectId, ready, name, version, app);
    }

    @PutMapping("/ns/service/scale")
    public BooleanWrapper scaleService(@RequestParam(required = false, defaultValue = "default") String namespace, @RequestParam(required = false) String name, @RequestParam int value) {
        kubernetesRuntimeUseCase.scaleService(namespace, name, value);
        return BooleanWrapper.TRUE;
    }

}
