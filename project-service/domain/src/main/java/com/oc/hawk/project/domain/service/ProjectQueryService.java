package com.oc.hawk.project.domain.service;

import com.oc.hawk.ddd.web.DomainPage;
import com.oc.hawk.project.domain.facade.UserFacade;
import com.oc.hawk.project.domain.model.user.UserInfo;
import com.oc.hawk.project.domain.model.project.Project;
import com.oc.hawk.project.domain.model.project.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProjectQueryService {
    private final UserFacade userFacade;
    private final ProjectRepository projectRepository;

    public DomainPage<Project> queryProject(int page, int size, String key) {

        UserInfo userInfo = userFacade.currentUser();

        Long departmentId = userInfo.isMaster() ? null : userInfo.getDepartmentId();
        return projectRepository.projectPage(page, size, key, departmentId);
    }


}

