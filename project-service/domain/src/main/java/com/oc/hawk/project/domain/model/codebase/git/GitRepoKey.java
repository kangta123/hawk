package com.oc.hawk.project.domain.model.codebase.git;

import com.oc.hawk.project.domain.model.project.ProjectID;

public class GitRepoKey {
    private final long projectId;

    public GitRepoKey(long projectId) {
        this.projectId = projectId;
    }

    public GitRepoKey(ProjectID projectId) {
        this.projectId = projectId.getId();
    }

    @Override
    public String toString() {
        return String.valueOf(projectId);
    }
}
