package com.oc.hawk.project.port.driving.facade.rest;

import com.oc.hawk.common.spring.mvc.BooleanWrapper;
import com.oc.hawk.ddd.web.DomainPage;
import com.oc.hawk.project.api.command.RegisterProjectCommand;
import com.oc.hawk.project.api.dto.ProjectDetailDTO;
import com.oc.hawk.project.api.dto.ProjectListItemDTO;
import com.oc.hawk.project.api.dto.ProjectNameDTO;
import com.oc.hawk.project.application.ProjectUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectUseCase projectUsecase;


    @GetMapping("/page")
    public DomainPage<ProjectListItemDTO> queryPagePage(@RequestParam(value = "page", required = false) Integer page,
                                                        @RequestParam(value = "size", required = false) Integer size,
                                                        @RequestParam(name = "key", required = false) String key) {
        return projectUsecase.queryProjectPage(page, size, key);
    }

    @GetMapping("/names")
    public List<ProjectNameDTO> getProjectNames(
        @RequestParam(name = "ids", required = false) List<Long> ids) {
        return projectUsecase.getProjectNames(ids);
    }

    @GetMapping("/ids")
    public List<Long> getProjectIds() {
        return projectUsecase.getProjectIds();
    }

    @GetMapping("/count")
    public BooleanWrapper getProjectTotalCount() {
        int count = projectUsecase.getProjectTotalCount();
        return new BooleanWrapper(true, count);
    }

    @GetMapping("/{id:[0-9]+}")
    public ProjectDetailDTO getProject(@PathVariable long id) {
        return projectUsecase.getProject(id);
    }
    @DeleteMapping("/{id:[0-9]+}")
    public BooleanWrapper deleteProject(@PathVariable long id) {
        projectUsecase.deleteProject(id);
        return BooleanWrapper.TRUE;
    }
    @PostMapping("/")
    public BooleanWrapper registerProject(@RequestBody RegisterProjectCommand command) {
        projectUsecase.registerProject(command);
        return BooleanWrapper.TRUE;
    }

}

