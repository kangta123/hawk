package com.oc.hawk.project.application.representation;

import com.oc.hawk.project.api.dto.ProjectAppDTO;
import com.oc.hawk.project.domain.model.projectapp.ProjectApp;
import org.springframework.stereotype.Component;

@Component
public class ProjectAppRepresentation {
    public ProjectAppDTO toProjectAppDTO(ProjectApp projectApp) {
        ProjectAppDTO projectAppDTO = new ProjectAppDTO();
        projectAppDTO.setAppName(projectApp.getAppName());
        projectAppDTO.setAppPath(projectApp.getAppPath());
        projectAppDTO.setId(projectApp.getId().getId());
        return projectAppDTO;
    }

}
