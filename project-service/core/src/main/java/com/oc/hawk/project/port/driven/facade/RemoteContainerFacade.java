package com.oc.hawk.project.port.driven.facade;

import com.google.common.collect.Maps;
import com.oc.hawk.container.api.dto.ProjectRuntimeStatDTO;
import com.oc.hawk.project.application.representation.facade.ContainerFacade;
import com.oc.hawk.project.domain.model.project.ProjectID;
import com.oc.hawk.project.port.driven.facade.feign.ContainerGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class RemoteContainerFacade implements ContainerFacade {
    private final ContainerGateway containerGateway;

    @Override
    public Map<ProjectID, ProjectRuntimeCounter> loadRuntimeCountByProjects(List<ProjectID> projectIds) {
        Map<ProjectID, ProjectRuntimeCounter> result = Maps.newHashMap();
        List<Long> ids = projectIds.stream().map(ProjectID::getId).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(ids)){
            return  result;
        }
        ProjectRuntimeStatDTO projectRuntimeStatDTO = containerGateway.loadRuntimeCountByProjects(ids);
        if (projectRuntimeStatDTO == null) {
            return result;
        }
        Map<Long, Integer> projectConfigurationCountMap = projectRuntimeStatDTO.getProjectConfigurationCountMap();
        Map<Long, Integer> projectRunnerCountMap = projectRuntimeStatDTO.getProjectRunnerCountMap();

        if (projectConfigurationCountMap != null) {
            projectConfigurationCountMap.forEach((projectId, configCount) -> {
                Integer count = projectRunnerCountMap.getOrDefault(projectId, 0);
                result.put(new ProjectID(projectId), new ProjectRuntimeCounter(configCount, count));
            });
        }
        return result;
    }
}
