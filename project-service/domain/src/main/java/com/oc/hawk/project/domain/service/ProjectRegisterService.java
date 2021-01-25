package com.oc.hawk.project.domain.service;

import com.oc.hawk.ddd.DomainService;
import com.oc.hawk.project.domain.model.codebase.CodeBase;
import com.oc.hawk.project.domain.model.codebase.CodeBaseID;
import com.oc.hawk.project.domain.model.codebase.CodeBaseRepository;
import com.oc.hawk.project.domain.model.codebase.git.GitCodeBase;
import com.oc.hawk.project.domain.model.project.Project;
import com.oc.hawk.project.domain.model.project.ProjectID;
import com.oc.hawk.project.domain.model.project.ProjectRepository;
import com.oc.hawk.project.domain.model.project.exception.ProjectRegisterIllegalArgumentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@DomainService
public class ProjectRegisterService {

    private final CodeBaseRepository codeBaseRepository;
    private final ProjectRepository projectRepository;


    public ProjectID register(Project project, CodeBase codeBase) {
        ProjectRegisterChecker projectRegisterChecker = new ProjectRegisterChecker(projectRepository);

        if (projectRegisterChecker.isDuplicate(project.getName())) {
            throw new ProjectRegisterIllegalArgumentException("项目名称已经存在");
        }

        CodeBaseID codeBaseId;
        if (codeBase instanceof GitCodeBase) {
            codeBaseId = codeBaseRepository.save(codeBase);
        } else {
            throw new ProjectRegisterIllegalArgumentException("不支持此类型代码库");
        }

        project.assignCodeBase(codeBaseId);
        return projectRepository.save(project);
    }
}
