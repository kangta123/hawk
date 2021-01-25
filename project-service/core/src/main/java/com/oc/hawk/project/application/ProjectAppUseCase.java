package com.oc.hawk.project.application;

import com.oc.hawk.project.api.command.UpdateProjectAppCommand;
import com.oc.hawk.project.api.dto.ProjectAppDTO;
import com.oc.hawk.project.application.representation.ProjectAppRepresentation;
import com.oc.hawk.project.domain.model.project.Project;
import com.oc.hawk.project.domain.model.project.ProjectID;
import com.oc.hawk.project.domain.model.project.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
@Slf4j
public class ProjectAppUseCase {
    private final ProjectRepository projectRepository;
    private final ProjectAppRepresentation projectAppRepresentation;

    @Transactional(rollbackFor = Exception.class)
    public void updateProjectApp(UpdateProjectAppCommand command) {
        log.info("update project {} app {}", command.getProjectId(), command.getAppPath());
        Project project = projectRepository.byId(new ProjectID(command.getProjectId()));
        project.addProjectApp(command.getBranch(), command.getAppName(), command.getAppPath());

        projectRepository.save(project);

    }

    public List<ProjectAppDTO> findProjectApps(long id) {
        Project project = projectRepository.byId(new ProjectID(id));
        if (project.getApps() != null) {
            return project.getApps().stream().map(projectAppRepresentation::toProjectAppDTO).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
