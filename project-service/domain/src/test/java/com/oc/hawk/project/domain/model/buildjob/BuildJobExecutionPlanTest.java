package com.oc.hawk.project.domain.model.buildjob;

import com.oc.hawk.project.ProjectBaseTest;
import com.oc.hawk.test.TestHelper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static com.oc.hawk.project.domain.model.buildjob.ProjectBuildState.*;

class BuildJobExecutionPlanTest extends ProjectBaseTest {
    @Test
    void addNewStage_addEndStageStateShouldEnd() {
        BuildJobExecutionPlan buildJobExecutionPlan = newExecutionPlan();
        buildJobExecutionPlan.addNewStage(JobStage.End, true, null);

        Assertions.assertThat(buildJobExecutionPlan.getState()).isEqualTo(END);
    }

    @Test
    void addNewStage_addFailStateWithEndTime() {
        BuildJobExecutionPlan buildJobExecutionPlan = newExecutionPlan();
        buildJobExecutionPlan.addNewStage(JobStage.Start, false, null);

        Assertions.assertThat(buildJobExecutionPlan.getEndTime()).isNotNull();
    }

    @Test
    void addNewStage_addFistStage() {
        BuildJobExecutionPlan buildJobExecutionPlan = newExecutionPlan();
        buildJobExecutionPlan.addNewStage(JobStage.Start, true, null);

        Assertions.assertThat(buildJobExecutionPlan.getStages()).hasSize(1);
        ProjectBuildStage stage = buildJobExecutionPlan.getStages().iterator().next();
        Assertions.assertThat(stage.getStage()).isEqualTo(JobStage.Start);
        Assertions.assertThat(stage.isSuccess()).isEqualTo(true);
    }

    @Test
    void addNewStage_addFailedStageExecutionShouldFailed() {
        BuildJobExecutionPlan buildJobExecutionPlan = newExecutionPlan();
        buildJobExecutionPlan.addNewStage(JobStage.DockerBuild, false, null);

        Assertions.assertThat(buildJobExecutionPlan.getState()).isEqualTo(FAILED);
    }


    @Test
    void addNewStage_changeJobStateToRunningIfStageIsStart() {
        BuildJobExecutionPlan buildJobExecutionPlan = newExecutionPlan();
        String image = TestHelper.anyString();
        buildJobExecutionPlan.addNewStage(JobStage.Start, true, new ProjectImage(image));

        Assertions.assertThat(buildJobExecutionPlan.getState()).isEqualByComparingTo(RUNNING);
    }

    @Test
    void addNewStage_addStageWithProjectImage() {
        BuildJobExecutionPlan buildJobExecutionPlan = newExecutionPlan();
        String image = TestHelper.anyString();
        buildJobExecutionPlan.addNewStage(JobStage.DockerBuild, false, new ProjectImage(image));

        Set<ProjectImage> images = buildJobExecutionPlan.getProjectImages();
        Assertions.assertThat(images).hasSize(1);
        Assertions.assertThat(images.iterator().next()).isEqualTo(new ProjectImage(image));
    }

    private BuildJobExecutionPlan newExecutionPlan() {
        return new BuildJobExecutionPlan(new ProjectBuildExecutionEnv(""));
    }

}
