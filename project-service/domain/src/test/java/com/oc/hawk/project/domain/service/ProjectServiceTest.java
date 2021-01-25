package com.oc.hawk.project.domain.service;

import com.oc.hawk.project.ProjectBaseTest;
import com.oc.hawk.project.domain.model.codebase.CodeBase;
import com.oc.hawk.project.domain.model.codebase.git.GitCodeBase;
import com.oc.hawk.project.domain.model.project.Project;
import com.oc.hawk.project.domain.model.project.exception.ProjectRegisterIllegalArgumentException;
import com.oc.hawk.test.TestHelper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.when;

class ProjectServiceTest extends ProjectBaseTest {

    @Test
    void registerProject_projectNameAlreadyExist() {
        Project project = getProject();

        when(new ProjectRegisterChecker(projectRepository).isDuplicate(project.getName())).thenReturn(true);
        Assertions.assertThatThrownBy(() -> {
            ProjectRegisterService projectRegisterService = new ProjectRegisterService(codeBaseRepository, projectRepository);
            projectRegisterService.register(project, getCodebase());
        }).isInstanceOf(ProjectRegisterIllegalArgumentException.class);
    }

    public Project getProject() {
        return TestHelper.newInstance(Project.class);
    }

    public CodeBase getCodebase() {
        return TestHelper.newInstance(GitCodeBase.class);
    }
}
