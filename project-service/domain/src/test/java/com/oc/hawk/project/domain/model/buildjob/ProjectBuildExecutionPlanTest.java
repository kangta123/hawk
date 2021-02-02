package com.oc.hawk.project.domain.model.buildjob;

import com.oc.hawk.project.ProjectBaseTest;
import com.oc.hawk.test.TestHelper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static com.oc.hawk.project.domain.model.buildjob.ProjectBuildState.*;

class ProjectBuildExecutionPlanTest extends ProjectBaseTest {
    @Test
    void addNewStage_addEndStageStateShouldEnd() {
        ProjectBuildExecutionPlan projectBuildExecutionPlan = newExecutionPlan();
        projectBuildExecutionPlan.addNewStage(JobStage.End, true, null);

        Assertions.assertThat(projectBuildExecutionPlan.getState()).isEqualTo(END);
    }

    @Test
    void addNewStage_addFailStateWithEndTime() {
        ProjectBuildExecutionPlan projectBuildExecutionPlan = newExecutionPlan();
        projectBuildExecutionPlan.addNewStage(JobStage.Start, false, null);

        Assertions.assertThat(projectBuildExecutionPlan.getEndTime()).isNotNull();
    }

    @Test
    void addNewStage_addFistStage() {
        ProjectBuildExecutionPlan projectBuildExecutionPlan = newExecutionPlan();
        projectBuildExecutionPlan.addNewStage(JobStage.Start, true, null);

        Assertions.assertThat(projectBuildExecutionPlan.getStages()).hasSize(1);
        ProjectBuildStage stage = projectBuildExecutionPlan.getStages().iterator().next();
        Assertions.assertThat(stage.getStage()).isEqualTo(JobStage.Start);
        Assertions.assertThat(stage.isSuccess()).isEqualTo(true);
    }

    @Test
    void addNewStage_addFailedStageExecutionShouldFailed() {
        ProjectBuildExecutionPlan projectBuildExecutionPlan = newExecutionPlan();
        projectBuildExecutionPlan.addNewStage(JobStage.DockerBuild, false, null);

        Assertions.assertThat(projectBuildExecutionPlan.getState()).isEqualTo(FAILED);
    }


    @Test
    void addNewStage_changeJobStateToRunningIfStageIsStart() {
        ProjectBuildExecutionPlan projectBuildExecutionPlan = newExecutionPlan();
        String image = TestHelper.anyString();
        projectBuildExecutionPlan.addNewStage(JobStage.Start, true, new ProjectImage(image));

        Assertions.assertThat(projectBuildExecutionPlan.getState()).isEqualByComparingTo(RUNNING);
    }

    @Test
    void addNewStage_addStageWithProjectImage() {
        ProjectBuildExecutionPlan projectBuildExecutionPlan = newExecutionPlan();
        String image = TestHelper.anyString();
        projectBuildExecutionPlan.addNewStage(JobStage.DockerBuild, false, new ProjectImage(image));

        Set<ProjectImage> images = projectBuildExecutionPlan.getProjectImages();
        Assertions.assertThat(images).hasSize(1);
        Assertions.assertThat(images.iterator().next()).isEqualTo(new ProjectImage(image));
    }

    private ProjectBuildExecutionPlan newExecutionPlan() {
        return new ProjectBuildExecutionPlan(new ProjectBuildExecutionEnv(""));
    }

}
