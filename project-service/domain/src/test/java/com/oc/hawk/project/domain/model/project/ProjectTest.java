package com.oc.hawk.project.domain.model.project;

import com.oc.hawk.project.ProjectBaseTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProjectTest extends ProjectBaseTest {
    @Test
    public void testAddProjectApp_ignoreRootPath() {
        final Project project = Project.builder().build();
        project.addProjectApp(str(), str(), Project.ROOT_PATH);
        Assertions.assertThat(project.getApps()).isEmpty();
    }

}
