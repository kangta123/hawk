package com.oc.hawk.traffic.port.driven.facade;

import java.util.List;

import org.springframework.stereotype.Component;

import com.oc.hawk.project.api.dto.ProjectDetailDTO;
import com.oc.hawk.traffic.application.entrypoint.representation.facade.ProjectFacade;
import com.oc.hawk.traffic.port.driven.facade.feign.ProjectGateway;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class RemoteProjectFacade implements ProjectFacade{
    
    private final ProjectGateway projectGateway;
    
    @Override
    public List<Long> queryVisibleProjectIds() {
        return projectGateway.queryVisibleProjectIds();
    }

    @Override
    public ProjectDetailDTO getProject(Long id) {
        return projectGateway.getProject(id);
    }
    

}
