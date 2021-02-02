package com.oc.hawk.container.runtime.port.driving.rest;

import com.oc.hawk.container.api.dto.ProjectSummaryDTO;
import com.oc.hawk.container.runtime.application.stat.ProjectRuntimeStatUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class RuntimeStatController {
    private final ProjectRuntimeStatUseCase projectRuntimeStatUseCase;

    @GetMapping("/project/summary")
    public ProjectSummaryDTO getProjectSummary() {
        return projectRuntimeStatUseCase.getProjectRuntimeStat();
    }
}
