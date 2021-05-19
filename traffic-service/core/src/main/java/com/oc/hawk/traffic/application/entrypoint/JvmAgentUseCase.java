package com.oc.hawk.traffic.application.entrypoint;

import com.oc.hawk.traffic.port.driven.facade.JvmAgentFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * @author kangta123
 */
@Service
@RequiredArgsConstructor
public class JvmAgentUseCase {
    private final JvmAgentFacade jvmAgentFacade;

    public void hotswap(InputStream inputStream, String instanceName, String namespace) {
        jvmAgentFacade.hotswap(inputStream, instanceName, namespace);
    }
}
