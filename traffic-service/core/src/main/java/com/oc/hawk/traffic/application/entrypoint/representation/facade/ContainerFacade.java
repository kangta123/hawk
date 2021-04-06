package com.oc.hawk.traffic.application.entrypoint.representation.facade;

import com.oc.hawk.container.api.dto.InstanceProjectDTO;

import java.util.List;

public interface ContainerFacade {
    List<InstanceProjectDTO> getProjectInstances(List<Long> projectIds);
}
