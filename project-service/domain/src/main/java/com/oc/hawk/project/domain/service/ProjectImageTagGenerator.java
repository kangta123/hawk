package com.oc.hawk.project.domain.service;

import com.oc.hawk.project.domain.model.buildjob.ProjectImageTag;
import com.oc.hawk.project.domain.model.project.ProjectID;

public interface ProjectImageTagGenerator {
   ProjectImageTag createImageTag(ProjectID projectId, String customTag);
}
