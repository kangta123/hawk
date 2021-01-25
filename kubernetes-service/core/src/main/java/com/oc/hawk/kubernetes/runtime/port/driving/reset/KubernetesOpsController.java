package com.oc.hawk.kubernetes.runtime.port.driving.reset;

import com.oc.hawk.common.spring.mvc.BooleanWrapper;
import com.oc.hawk.infrastructure.application.KubernetesApi;
import io.fabric8.kubernetes.api.model.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ops")
@RequiredArgsConstructor
@Slf4j
public class KubernetesOpsController {
    private final KubernetesApi kubernetesApi;

    @GetMapping("srv")
    public BooleanWrapper queryUnAvailableSvc() {
        List<Service> services = kubernetesApi.getSrv("default");

        for (Service service : services) {
            System.out.println(12);
        }
        return BooleanWrapper.TRUE;
    }
}
