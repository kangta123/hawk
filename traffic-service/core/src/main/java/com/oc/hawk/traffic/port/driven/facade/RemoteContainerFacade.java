package com.oc.hawk.traffic.port.driven.facade;

import java.util.List;

import org.springframework.stereotype.Component;

import com.oc.hawk.container.api.dto.InstanceProjectDTO;
import com.oc.hawk.traffic.application.entrypoint.representation.facade.ContainerFacade;
import com.oc.hawk.traffic.port.driven.facade.feign.ContainerGateway;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class RemoteContainerFacade implements ContainerFacade{
    
    private final ContainerGateway containerGateway;

    @Override
    public List<InstanceProjectDTO> getProjectInstances(List<Long> projectIds) {
        return containerGateway.getProjectInstances(projectIds);
    }
    
}
