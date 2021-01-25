package com.oc.hawk.container.runtime.application.stat;

import com.google.common.collect.Lists;
import com.oc.hawk.container.api.dto.ProjectRuntimeStatDTO;
import com.oc.hawk.container.api.dto.ProjectSummaryDTO;
import com.oc.hawk.container.domain.config.ContainerConfiguration;
import com.oc.hawk.container.domain.facade.InfrastructureLifeCycleFacade;
import com.oc.hawk.container.domain.facade.ProjectFacade;
import com.oc.hawk.container.domain.model.runtime.config.InstanceConfigRepository;
import com.oc.hawk.container.domain.model.runtime.stat.RuntimeStatRepository;
import com.oc.hawk.container.runtime.application.stat.representation.RuntimeStatRepresentation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProjectRuntimeStatUseCase {
    private final RuntimeStatRepository runtimeStatRepository;
    private final RuntimeStatRepresentation runtimeStatRepresentation;
    private final ProjectFacade projectFacade;
    private final InfrastructureLifeCycleFacade infrastructureFacade;
    private final InstanceConfigRepository configurationRepository;

    private final ContainerConfiguration containerConfiguration;

    @PostConstruct
    public void startup() {
        resetRuntimeStat();
    }

    public void resetRuntimeStat() {
        Map<Long, Integer> projectRuntimeCount = infrastructureFacade.countRuntimeByProject(null);
        if (projectRuntimeCount != null) {
            runtimeStatRepository.reset(projectRuntimeCount);
        }
    }

    public ProjectSummaryDTO getProjectRuntimeStat() {
        List<Long> projectIds = projectFacade.getProjectIds();
        int totalRuntimeCount = runtimeStatRepository.getTotalRuntimeCount();

        Map<Long, Integer> runtimeCountByProject = runtimeStatRepository.getRuntimeCountByProject(projectIds);
        Integer visibleRuntimeCount = runtimeCountByProject.values().stream().reduce(Math::addExact).orElse(0);

        Integer projectTotalCount = projectFacade.getProjectTotalCount();
        return runtimeStatRepresentation.toRuntimeStatDTO(totalRuntimeCount, visibleRuntimeCount, projectTotalCount);
    }


    public ProjectRuntimeStatDTO queryProjectSummary(String namespace, List<Long> projectIds) {
        if (CollectionUtils.isEmpty(projectIds)) {
            return null;
        }
        if (StringUtils.isEmpty(namespace)) {
            namespace = containerConfiguration.getDefaultInstanceNamespace();
        }

        Map<Long, Integer> projectConfigCount = configurationRepository.countInstanceByProjects(projectIds, namespace);
        Map<Long, Integer> projectPodCount = runtimeStatRepository.getRuntimeCountByProject(Lists.newArrayList(projectConfigCount.keySet()));
        return new ProjectRuntimeStatDTO(projectPodCount, projectConfigCount);
    }
}
