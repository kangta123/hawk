package com.oc.hawk.project.port.driving.facade.rest;

import com.oc.hawk.project.api.dto.ProjectAppDTO;
import com.oc.hawk.project.application.ProjectAppUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ProjectAppController {
    private final ProjectAppUseCase projectAppUseCase;
    @GetMapping("/{id}/apps")
    public List<ProjectAppDTO> findProjectApps(@PathVariable long id) {
        return projectAppUseCase.findProjectApps(id);
    }

}

