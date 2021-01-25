package com.oc.hawk.container.runtime.port.driving.rest;

import com.oc.hawk.container.api.dto.ProjectRuntimeStatDTO;
import com.oc.hawk.container.runtime.application.stat.ProjectRuntimeStatUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/project/runtime")
@RequiredArgsConstructor
public class ProjectStatController {

    private final ProjectRuntimeStatUseCase projectRuntimeStatUseCase;

    @GetMapping("/summary")
    public ProjectRuntimeStatDTO queryProjectSummary(@RequestParam List<Long> projectIds, @RequestParam(required = false) String namespace) {
        return projectRuntimeStatUseCase.queryProjectSummary(namespace, projectIds);
    }

}
