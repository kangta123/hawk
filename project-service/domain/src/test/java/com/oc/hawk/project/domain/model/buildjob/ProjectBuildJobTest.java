package com.oc.hawk.project.domain.model.buildjob;

import com.google.common.collect.ImmutableMap;
import com.oc.hawk.project.ProjectBaseTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

class ProjectBuildJobTest extends ProjectBaseTest {

    @Test
    void ready_executionPlanEnvNotEmpty() {
        Map<String, Object> s = ImmutableMap.of("a", "1", "b", "2");
        ProjectBuildJob projectBuildJob = getProjectBuildJob();
        projectBuildJob.ready(new ProjectBuildExecutionEnv(s));


        BuildJobExecutionPlan executionPlan = projectBuildJob.getExecutionPlan();
        Assertions.assertThat(executionPlan).isNotNull();
        Assertions.assertThat(executionPlan.getEnv().envStr()).isEqualTo("{\"a\":\"1\",\"b\":\"2\"}");
    }

    @Test
    void ready_jobStageIncludeCreateAndReady() {
        ProjectBuildJob projectBuildJob = getProjectBuildJob();
        projectBuildJob.ready(new ProjectBuildExecutionEnv(""));

        BuildJobExecutionPlan executionPlan = projectBuildJob.getExecutionPlan();
        Assertions.assertThat(executionPlan.getStages()).extracting("stage").contains(JobStage.Created, JobStage.Ready);
    }

    @Test
    void start_executionPlanStateIsStart() {
        ProjectBuildJob projectBuildJob = getProjectBuildJob();
        projectBuildJob.ready(new ProjectBuildExecutionEnv(""));

        Assertions.assertThat(projectBuildJob.getExecutionPlan().getState()).isEqualTo(ProjectBuildState.START);
    }

    private ProjectBuildJob getProjectBuildJob() {
        return ProjectBuildJob.builder().executionPlan(BuildJobExecutionPlan.createNew()).build();
    }

    @Test
    void addNewStage_AddEndStageShouldSetJobEndTime() {
        ProjectBuildJob projectBuildJob = getProjectBuildJob();
        projectBuildJob.ready(new ProjectBuildExecutionEnv(""));

        projectBuildJob.addNewStage(JobStage.End, true, null);

        Assertions.assertThat(projectBuildJob.getExecutionPlan().getEndTime()).isNotNull();
    }

}
