package com.oc.hawk.traffic.application.entrypoint.representation.facade;

import java.util.List;
import com.oc.hawk.container.api.dto.InstanceProjectDTO;

public interface ContainerFacade {
    List<InstanceProjectDTO> getProjectInstances(List<Long> projectIds);
}
