package com.oc.hawk.project.application;

import com.oc.hawk.project.api.command.RegisterProjectCommand;
import com.oc.hawk.project.domain.model.project.BuildType;
import com.oc.hawk.project.domain.model.project.Project;
import com.oc.hawk.project.domain.model.project.ProjectRuntime;
import com.oc.hawk.project.domain.model.user.UserDepartment;
import com.oc.hawk.project.domain.model.user.UserInfo;
import com.oc.hawk.test.TestHelper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.when;

class ProjectFactoryTest extends ProjectBaseTest {

    ProjectFactory projectFactory;

    @BeforeEach
    public void setup() {
        projectFactory = new ProjectFactory(userFacade);
        when(userFacade.currentUserDepartment()).thenReturn(TestHelper.newInstance(UserDepartment.class));
        when(userFacade.currentUser()).thenReturn(TestHelper.newInstance(UserInfo.class));
    }

    @Test
    @DisplayName("Project builder in project module created by factory cannot be null")
    void create_projectBuilderNotNull() {
        RegisterProjectCommand command = registerProjectCommand();

        Project project = projectFactory.create(command);
        Assertions.assertThat(project).isNotNull();
        Assertions.assertThat(project.getBuild()).isNotNull();
    }

    RegisterProjectCommand registerProjectCommand() {
        RegisterProjectCommand registerProjectCommand = TestHelper.newInstance(RegisterProjectCommand.class);
        registerProjectCommand.setBuildType(String.valueOf(BuildType.MAVEN));
        registerProjectCommand.setProjectType(String.valueOf(ProjectRuntime.SPRINGBOOT));
        return registerProjectCommand;
    }

    @Test
    void createMavenBuildProjectBuildCommandNotEmpty() {
        RegisterProjectCommand command = registerProjectCommand();
        command.setBuildCommand(null);
        Project project = projectFactory.create(command);

        Assertions.assertThat(project.getBuild().getCommand().getCommand()).isNotEmpty();

    }
}
