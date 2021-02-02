package com.oc.hawk.project.domain.model.buildjob;

import com.oc.hawk.project.domain.model.project.ProjectID;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface ProjectBuildJobRepository {
    ProjectBuildJob save(ProjectBuildJob projectBuildJob);

    Integer findProjectBuildTimesByDay(ProjectID projectId);

    Optional<ProjectBuildJob> byId(ProjectBuildJobID projectId);

    boolean isCustomTagExisted(String customTag, ProjectID projectId);

    List<ProjectBuildJob> queryLatestBuildJobs(long projectId, int size);

    Map<ProjectID, ProjectBuildJob> findProjectLastBuildJob(List<ProjectID> projectIds);

    Set<InstanceImageInfo> queryInstanceImages(long projectId, String tag);
}

