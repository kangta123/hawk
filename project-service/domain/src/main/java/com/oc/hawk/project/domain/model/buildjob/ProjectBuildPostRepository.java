package com.oc.hawk.project.domain.model.buildjob;

public interface ProjectBuildPostRepository {
    void save(ProjectBuildPost projectBuildPost);

    ProjectBuildPost byBuildJobId(long jobId);
}
