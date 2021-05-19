package com.oc.hawk.traffic.port.driven.facade;

import java.io.InputStream;

/**
 * @author kangta123
 */
public interface JvmAgentFacade {
    void hotswap(InputStream inputStream, String instanceName, String namespace) ;
}
