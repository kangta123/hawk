package com.oc.hawk.container.runtime.application.instance.representation;

import com.oc.hawk.container.domain.model.runtime.config.InstanceName;
import com.oc.hawk.kubernetes.api.constants.RuntimeInfoDTO;

import java.util.List;

public interface RuntimeInfoFacade {
    List<RuntimeInfoDTO> queryRuntimeInfo(String namespace, Long projectId);

    List<RuntimeInfoDTO> queryRuntimeInfo(String namespace, InstanceName instanceName);


}
