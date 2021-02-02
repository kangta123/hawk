package com.oc.hawk.project.application.representation;

import com.oc.hawk.project.api.dto.ProjectBuildJobDTO;
import com.oc.hawk.project.api.dto.ProjectBuildJobDetailDTO;
import com.oc.hawk.project.api.dto.ProjectBuildJobStageDTO;
import com.oc.hawk.project.domain.model.buildjob.*;
import com.oc.hawk.project.domain.model.codebase.git.GitCommitLog;
import com.oc.hawk.project.domain.model.gitrecord.GitCommitLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProjectBuildJobRepresentation {
    private final CodeBaseRepresentation codeBaseRepresentation;
    private final GitCommitLogRepository gitCommitLogRepository;
    private final ProjectBuildPostRepository projectBuildPostRepository;


    public ProjectBuildJobDTO toProjectBuildJobDTO(ProjectBuildJob job) {
        return toProjectBuildJobDTO(job, new ProjectBuildJobDTO());
    }

    public ProjectBuildJobDTO toProjectBuildJobDTO(ProjectBuildJob job, ProjectBuildJobDTO projectBuildJobDTO) {
        transfer(job, projectBuildJobDTO);
        return projectBuildJobDTO;
    }

    private void transfer(ProjectBuildJob job, ProjectBuildJobDTO projectBuildJobDTO) {
        GitCommitLog gitCommitLog = null;
        if (job.getGitCommitLogId() != null) {
            gitCommitLog = gitCommitLogRepository.byId(job.getGitCommitLogId().getId());
        }
        projectBuildJobDTO.setCreatedTime(job.getCreatedAt());
        if (job.getExecutionPlan() != null) {
            projectBuildJobDTO.setEndTime(job.getExecutionPlan().getEndTime());
        }
        projectBuildJobDTO.setId(job.getId().getId());
        projectBuildJobDTO.setProjectId(job.getProjectId().getId());
        projectBuildJobDTO.setTag(job.getImageTag().getTag());

        JobCreator creator = job.getCreator();
        projectBuildJobDTO.setCreator(creator.getCreateId().getId());
        projectBuildJobDTO.setCreatorName(creator.getName());

        ProjectBuildExecutionPlan executionPlan = job.getExecutionPlan();
        if (executionPlan != null) {
            projectBuildJobDTO.setEndTime(executionPlan.getEndTime());
            if (executionPlan.getState() != null) {
                projectBuildJobDTO.setState(executionPlan.getState().toString());
            }
            executionPlan.getProjectImages().forEach(i -> projectBuildJobDTO.addApp(i.getApp()));
        }

        final ProjectBuildPost projectBuildPost = projectBuildPostRepository.byBuildJobId(job.getId().getId());
        if (projectBuildPost != null) {
            projectBuildJobDTO.setDeployToId(projectBuildPost.getDeployTo());
            projectBuildJobDTO.setDeployToInstance(projectBuildPost.getDeployToInstance());
        }
        transferToJobDetail(projectBuildJobDTO, gitCommitLog, executionPlan);
    }

    private void transferToJobDetail(ProjectBuildJobDTO projectBuildJobDTO, GitCommitLog commitLogRecord, ProjectBuildExecutionPlan executionPlan) {
        if (projectBuildJobDTO instanceof ProjectBuildJobDetailDTO) {
            ProjectBuildJobDetailDTO detail = (ProjectBuildJobDetailDTO) projectBuildJobDTO;

            if (commitLogRecord != null) {
                projectBuildJobDTO.setBranch(commitLogRecord.getBranch());
                detail.setRecord(codeBaseRepresentation.toGitCommitRecordDTO(commitLogRecord));
            }
            if (executionPlan != null) {
                List<ProjectBuildJobStageDTO> stages = executionPlan.getStages().stream().map(projectBuildStage -> {
                    ProjectBuildJobStageDTO stage = new ProjectBuildJobStageDTO();
                    stage.setSuccess(projectBuildStage.isSuccess());
                    stage.setJobStage(projectBuildStage.getStage().toString());
                    stage.setStartTime(projectBuildStage.getCreatedAt());
                    //assume that data is image app
                    stage.setData(new ProjectImageApp(projectBuildStage.getData()).getApp());
                    stage.setTitle(projectBuildStage.getStage().getTitle());
                    return stage;
                }).collect(Collectors.toList());
                detail.setStages(stages);

            }
        }


    }


}
