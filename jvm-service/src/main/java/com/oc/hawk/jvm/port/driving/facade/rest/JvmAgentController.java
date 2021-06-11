package com.oc.hawk.jvm.port.driving.facade.rest;

import com.oc.hawk.common.spring.mvc.BooleanWrapper;
import com.oc.hawk.jvm.application.JvmAgentUseCase;
import com.oc.hawk.jvm.application.representation.ClassInfoDTO;
import com.oc.hawk.jvm.application.representation.JvmDashboardDTO;
import com.oc.hawk.jvm.application.representation.JvmThreadDTO;
import com.oc.hawk.jvm.application.representation.ThreadStackDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author kangta123
 */
@RestController
@RequestMapping("/agent")
@RequiredArgsConstructor
@Slf4j
public class JvmAgentController {
    private final JvmAgentUseCase jvmAgentUseCase;

    @PostMapping("/hotswap")
    public BooleanWrapper hotswapClass(
            @RequestParam String instanceName,
            @RequestParam(defaultValue = "default", required = false) String namespace,
            @RequestParam("file") MultipartFile file) throws Exception {

        jvmAgentUseCase.hotswap(file.getInputStream(), instanceName, namespace);
        return BooleanWrapper.TRUE;
    }

    @GetMapping("/dashboard")
    public JvmDashboardDTO dashboard(
            @RequestParam String instanceName,
            @RequestParam(defaultValue = "default", required = false) String namespace) {
        return jvmAgentUseCase.dashboard(instanceName, namespace);
    }

    @GetMapping("/class/decompile")
    public ClassInfoDTO decompileClass(
            @RequestParam String instanceName,
            @RequestParam String clz,
            @RequestParam(defaultValue = "default", required = false) String namespace) {
        return jvmAgentUseCase.decompileClass(clz, instanceName, namespace);
    }

    @GetMapping("/thread/{id}/stack")
    public ThreadStackDTO getThreadTrace(
            @PathVariable("id") long id,
            @RequestParam String instanceName,
            @RequestParam(defaultValue = "default", required = false) String namespace) {
        return jvmAgentUseCase.getThreadStack(id, instanceName, namespace);
    }
    @GetMapping("/thread")
    public List<JvmThreadDTO> getThreadTrace(
            @RequestParam String instanceName,
            @RequestParam(defaultValue = "default", required = false) String namespace) {
        return jvmAgentUseCase.getThreadList(instanceName, namespace);
    }
}
