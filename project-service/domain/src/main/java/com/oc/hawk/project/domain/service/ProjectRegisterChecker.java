package com.oc.hawk.project.domain.service;

import com.oc.hawk.ddd.DomainService;
import com.oc.hawk.project.domain.model.project.ProjectName;
import com.oc.hawk.project.domain.model.project.ProjectRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@DomainService
public class ProjectRegisterChecker {
    private final ProjectRepository projectRepository;

    public boolean isDuplicate(ProjectName projectName) {
        return projectRepository.existsProjectName(projectName);
    }
}

