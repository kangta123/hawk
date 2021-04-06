package com.oc.hawk.traffic.application.entrypoint.representation.facade;

import java.util.List;
import com.oc.hawk.project.api.dto.ProjectDetailDTO;

public interface ProjectFacade {

    List<Long> queryVisibleProjectIds();
    
    ProjectDetailDTO getProject(Long id);
}
