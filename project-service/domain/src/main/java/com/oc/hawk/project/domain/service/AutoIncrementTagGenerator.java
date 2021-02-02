package com.oc.hawk.project.domain.service;

import com.oc.hawk.project.domain.model.buildjob.ProjectBuildJobRepository;
import com.oc.hawk.project.domain.model.buildjob.ProjectImageTag;
import com.oc.hawk.project.domain.model.project.ProjectID;
import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AutoIncrementTagGenerator implements ProjectImageTagGenerator {
    private final ProjectBuildJobRepository projectBuildJobRepository;

    public AutoIncrementTagGenerator(ProjectBuildJobRepository projectBuildJobRepository) {
        this.projectBuildJobRepository = projectBuildJobRepository;
    }

    @Override
    public ProjectImageTag createImageTag(ProjectID projectId, String customTag) {
        if (StringUtils.isEmpty(customTag)) {
            Integer times = projectBuildJobRepository.findProjectBuildTimesByDay(projectId);
            customTag = LocalDate.now().format(DateTimeFormatter.ofPattern("MMdd")) + new DecimalFormat("000").format(times);
        }
        return new ProjectImageTag(customTag);
    }
}
