package com.oc.hawk.project.application.representation;

import com.oc.hawk.common.utils.DateUtils;
import com.oc.hawk.ddd.web.DomainPage;
import com.oc.hawk.project.api.dto.ProjectDetailDTO;
import com.oc.hawk.project.api.dto.ProjectListItemDTO;
import com.oc.hawk.project.application.representation.facade.ContainerFacade;
import com.oc.hawk.project.domain.model.buildjob.ProjectBuildJob;
import com.oc.hawk.project.domain.model.buildjob.ProjectBuildJobRepository;
import com.oc.hawk.project.domain.model.buildjob.ProjectBuildState;
import com.oc.hawk.project.domain.model.codebase.CodeBase;
import com.oc.hawk.project.domain.model.project.Project;
import com.oc.hawk.project.domain.model.project.ProjectID;
import com.oc.hawk.project.port.driven.facade.ProjectRuntimeCounter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class ProjectRepresentation {
    private final ProjectBuildJobRepository projectBuildJobRepository;
    private final ContainerFacade containerFacade;


    public List<ProjectListItemDTO> toListDTO(List<Project> list) {
        List<ProjectID> projectIds = list.stream().map(Project::getProjectId).collect(Collectors.toList());
        Map<ProjectID, ProjectBuildJob> projectLastBuildJob = projectBuildJobRepository.findProjectLastBuildJob(projectIds);
        Map<ProjectID, ProjectRuntimeCounter> counterMap = containerFacade.loadRuntimeCountByProjects(projectIds);

        return list.stream().map(project -> {
            ProjectID projectId = project.getProjectId();

            ProjectListItemDTO projectListItemDTO = new ProjectListItemDTO();
            projectListItemDTO.setDescn(project.getDescn());
            projectListItemDTO.setId(projectId.getId());
            projectListItemDTO.setName(String.valueOf(project.getName()));
            projectListItemDTO.setProjectType(String.valueOf(project.getRuntime()));

            loadLatestBuildJob(projectLastBuildJob.getOrDefault(projectId, null), projectListItemDTO);
            loadProjectRuntimeCounter(counterMap.getOrDefault(projectId, null), projectListItemDTO);

            return projectListItemDTO;
        }).collect(Collectors.toList());
    }

    public DomainPage<ProjectListItemDTO> toPageDto(DomainPage<Project> page) {
        List<Project> content = page.getContent();
        List<ProjectListItemDTO> dtoList = toListDTO(content);
        return page.map(dtoList);
    }

    public ProjectDetailDTO toProjectDTO(Project project, CodeBase codeBase) {
        ProjectDetailDTO projectDetailDTO = new ProjectDetailDTO();
        projectDetailDTO.setBuildCommand(project.buildCommand());
        projectDetailDTO.setBuildType(project.buildType());
        projectDetailDTO.setDepartmentId(project.getDepartmentId());
        projectDetailDTO.setDescn(project.getDescn());
        ProjectID projectId = project.getProjectId();
        if (projectId != null) {
            projectDetailDTO.setId(projectId.getId());
        }
        projectDetailDTO.setName(project.getName().getName());
        projectDetailDTO.setProjectType(String.valueOf(project.getRuntime()));

        projectDetailDTO.setUrl(codeBase.url(true));
        return projectDetailDTO;
    }

    private void loadProjectRuntimeCounter(ProjectRuntimeCounter counter, ProjectListItemDTO projectListItemDTO) {
        if (counter != null) {
            projectListItemDTO.setConfigCount(counter.getConfigCount());
            projectListItemDTO.setRunningCount(counter.getRunningCount());
        }
    }

    private void loadLatestBuildJob(ProjectBuildJob buildJob, ProjectListItemDTO projectListItemDTO) {
        if (buildJob != null) {
            projectListItemDTO.setLastBuildTime(DateUtils.toLong(buildJob.getCreatedAt()));
            ProjectBuildState state = buildJob.getExecutionPlan().getState();
            projectListItemDTO.setState(String.valueOf(state));
        }
    }
}
