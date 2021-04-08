package com.oc.hawk.traffic.port.driven.facade;

import java.util.List;

import org.springframework.stereotype.Component;

import com.google.common.base.Joiner;
import com.oc.hawk.container.api.dto.InstanceConfigDTO;
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
        String projectIdList = Joiner.on(",").join(projectIds);
        return containerGateway.getProjectInstances(projectIdList);
    }

    @Override
    public InstanceConfigDTO getById(Long id) {
        return containerGateway.getById(id);
    }
    
}
