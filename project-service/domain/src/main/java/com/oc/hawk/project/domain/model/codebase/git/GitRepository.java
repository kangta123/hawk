package com.oc.hawk.project.domain.model.codebase.git;

import com.oc.hawk.project.domain.model.codebase.CodeBase;

import java.util.List;

public interface GitRepository {
    List<String> loadProjectBranches(GitRepoKey key, CodeBase codeBase);

    GitCommitLog branchLatestCommitLog(GitRepoKey key, String branchName, CodeBase gitCodeBase);

    void updateCodeRepo(GitRepoKey key, CodeBase codeBase);

}
