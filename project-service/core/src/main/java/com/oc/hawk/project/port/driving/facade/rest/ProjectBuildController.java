package com.oc.hawk.project.port.driving.facade.rest;

import com.oc.hawk.common.spring.mvc.BooleanWrapper;
import com.oc.hawk.project.api.command.CreateProjectBuildJobCommand;
import com.oc.hawk.project.api.dto.InstanceImageDTO;
import com.oc.hawk.project.api.dto.ProjectBuildJobDTO;
import com.oc.hawk.project.application.ProjectBuildJobUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/build/job")
@RequiredArgsConstructor
public class ProjectBuildController {
    private final ProjectBuildJobUseCase projectBuildJobUseCase;

    @PostMapping("")
    public ProjectBuildJobDTO createProjectBuildJob(@RequestBody CreateProjectBuildJobCommand command) {
        return projectBuildJobUseCase.createProjectBuildJob(command);
    }


    @GetMapping("")
    public List<ProjectBuildJobDTO> queryBuildJobs(@RequestParam("projectId") long projectId) {
        return projectBuildJobUseCase.queryBuildJobs(projectId);
    }

    @GetMapping("/tags")
    public List<String> getAllTags() {
        return projectBuildJobUseCase.getAllBuildTags();
    }

    @GetMapping("/images")
    public List<InstanceImageDTO> queryInstanceImages(@RequestParam("projectId") long projectId, @RequestParam(name = "tag", required = false) String tag) {
        return projectBuildJobUseCase.queryInstanceImages(projectId, tag);
    }

    @GetMapping("/{id:[0-9]+}")
    public ProjectBuildJobDTO getProjectBuildJob(@PathVariable("id") long jobId) {
        return projectBuildJobUseCase.getBuildJob(jobId);
    }

    @PutMapping("/{id}/stop")
    public BooleanWrapper stopProjectBuildJob(@PathVariable("id") long jobId) {
        projectBuildJobUseCase.stopProjectBuildJob(jobId);
        return BooleanWrapper.TRUE;
    }
}
