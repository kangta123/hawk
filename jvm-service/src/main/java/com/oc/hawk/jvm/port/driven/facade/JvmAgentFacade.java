package com.oc.hawk.jvm.port.driven.facade;

import com.oc.hawk.jvm.application.representation.ClassInfoDTO;
import com.oc.hawk.jvm.application.representation.JvmDashboardDTO;
import com.oc.hawk.jvm.application.representation.ThreadStackDTO;

import java.io.InputStream;

/**
 * @author kangta123
 */
public interface JvmAgentFacade {
    void hotswap(InputStream inputStream, String instanceName, String namespace) ;

    JvmDashboardDTO dashboard(String instanceName, String namespace);

    ClassInfoDTO decompileClass(String clz, String instanceName, String namespace) ;

    ThreadStackDTO getThreadTrace(long id, String instanceName, String namespace);
}
