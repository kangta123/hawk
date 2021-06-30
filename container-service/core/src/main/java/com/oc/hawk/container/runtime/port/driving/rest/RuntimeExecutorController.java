package com.oc.hawk.container.runtime.port.driving.rest;

import com.oc.hawk.common.spring.mvc.BooleanWrapper;
import com.oc.hawk.container.domain.model.runtime.config.InstanceId;
import com.oc.hawk.container.runtime.application.instance.InstanceExecutorUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/service")
@RequiredArgsConstructor
public class RuntimeExecutorController {
    private final InstanceExecutorUseCase instanceExecutorUseCase;

    @PutMapping("/start/{configId}")
    public BooleanWrapper startService(@PathVariable("configId") long configId) {
        instanceExecutorUseCase.startOrUpdate(new InstanceId(configId));
        return BooleanWrapper.TRUE;
    }

    @PutMapping("/stop/{id}")
    public BooleanWrapper stopService(@PathVariable long id) {
        instanceExecutorUseCase.stopInstance(new InstanceId(id));
        return BooleanWrapper.TRUE;
    }

}
