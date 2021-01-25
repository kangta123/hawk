package com.oc.hawk.container.runtime.application.stat.representation;

import com.oc.hawk.container.api.dto.ProjectSummaryDTO;
import org.springframework.stereotype.Component;

@Component
public class RuntimeStatRepresentation {
    public ProjectSummaryDTO toRuntimeStatDTO(int totalRuntimeCount, int visibleRuntimeCount, int totalProjectCount) {
        ProjectSummaryDTO projectSummaryDTO = new ProjectSummaryDTO();
        projectSummaryDTO.setTotalRunner(totalRuntimeCount);
        projectSummaryDTO.setTotalConfiguration(visibleRuntimeCount);
        projectSummaryDTO.setTotalProject(totalProjectCount);
        return projectSummaryDTO;
    }
}
