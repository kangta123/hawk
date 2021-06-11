package com.oc.hawk.jvm.application;

import com.oc.hawk.jvm.application.representation.ClassInfoDTO;
import com.oc.hawk.jvm.application.representation.JvmDashboardDTO;
import com.oc.hawk.jvm.application.representation.JvmThreadDTO;
import com.oc.hawk.jvm.application.representation.ThreadStackDTO;
import com.oc.hawk.jvm.port.driven.facade.JvmAgentFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

/**
 * @author kangta123
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class JvmAgentUseCase {
    private final JvmAgentFacade jvmAgentFacade;

    public void hotswap(InputStream inputStream, String instanceName, String namespace) {
        log.info("[jvm-agent] hotswap {}.{}", instanceName, namespace);
        jvmAgentFacade.hotswap(inputStream, instanceName, namespace);
    }

    public JvmDashboardDTO dashboard(String instanceName, String namespace) {
        log.info("[jvm-agent] view dashboard {}.{}", instanceName, namespace);
        return jvmAgentFacade.dashboard(instanceName, namespace);
    }

    public ClassInfoDTO decompileClass(String clz, String instanceName, String namespace) {
        log.info("[jvm-agent] get class info {} {}.{}", clz, instanceName, namespace);
        return jvmAgentFacade.decompileClass(clz, instanceName, namespace);
    }

    public ThreadStackDTO getThreadStack(long id, String instanceName, String namespace) {
        log.info("[jvm-agent] get thread {} trace {}.{}", id, instanceName, namespace);
        return jvmAgentFacade.getThreadTrace(id, instanceName, namespace);
    }

    public List<JvmThreadDTO> getThreadList(String instanceName, String namespace) {
        log.info("[jvm-agent] get thread list  {}.{}", instanceName, namespace);
        return jvmAgentFacade.getThreadList(instanceName, namespace);
    }
}
