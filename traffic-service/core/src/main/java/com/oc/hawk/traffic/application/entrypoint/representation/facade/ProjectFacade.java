package com.oc.hawk.traffic.application.entrypoint.representation.facade;

import com.oc.hawk.project.api.dto.ProjectDetailDTO;

import java.util.List;

public interface ProjectFacade {

    List<Long> queryVisibleProjectIds();

    ProjectDetailDTO getProject(Long id);
}
