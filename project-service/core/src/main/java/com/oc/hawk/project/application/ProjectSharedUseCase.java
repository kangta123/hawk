package com.oc.hawk.project.application;

import com.google.common.base.Joiner;
import com.oc.hawk.project.api.dto.ProjectListItemDTO;
import com.oc.hawk.project.application.representation.ProjectRepresentation;
import com.oc.hawk.project.domain.model.project.Project;
import com.oc.hawk.project.domain.model.project.ProjectID;
import com.oc.hawk.project.domain.model.shared.ProjectSharedRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProjectSharedUseCase {
    private final ProjectSharedRepository projectSharedRepository;
    private final ProjectRepresentation projectRepresentation;

    public List<ProjectListItemDTO> getProjectSharedToMe() {
        List<Project> projects = projectSharedRepository.getProjectSharedToMe();
        if (projects == null || projects.isEmpty()) {
            return null;
        }
        return projectRepresentation.toListDTO(projects);
    }

    @Transactional(rollbackFor = Exception.class)
    public void sharedToUser(long id, List<Long> users) {
        log.info("project {} shared to user {}", id, Joiner.on(",").join(users));
        projectSharedRepository.sharedToUser(new ProjectID(id), users);
    }

    public List<Long> getProjectSharedUsers(long id) {
        return projectSharedRepository.projectSharedUsers(new ProjectID(id));
    }
}
