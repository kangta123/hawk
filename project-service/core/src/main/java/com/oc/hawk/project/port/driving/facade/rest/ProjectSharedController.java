package com.oc.hawk.project.port.driving.facade.rest;


import com.oc.hawk.common.spring.mvc.BooleanWrapper;
import com.oc.hawk.project.api.dto.ProjectListItemDTO;
import com.oc.hawk.project.application.ProjectSharedUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ProjectSharedController {
    private final ProjectSharedUseCase projectSharedUseCase;

    @GetMapping("/shared")
    public List<ProjectListItemDTO> getProjectSharedToMe() {
        return projectSharedUseCase.getProjectSharedToMe();
    }

    @GetMapping("/{id}/users")
    public List<Long> sharedToUser(@PathVariable("id") long id) {
        return projectSharedUseCase.getProjectSharedUsers(id);
    }

    @PutMapping("/{id}/users")
    public BooleanWrapper sharedToUser(@PathVariable("id") long id,
                                       @RequestParam("users") List<Long> users) {
        projectSharedUseCase.sharedToUser(id, users);
        return BooleanWrapper.TRUE;
    }
}

