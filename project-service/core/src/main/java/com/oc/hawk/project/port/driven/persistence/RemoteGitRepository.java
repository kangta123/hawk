package com.oc.hawk.project.port.driven.persistence;

import com.oc.hawk.common.utils.DateUtils;
import com.oc.hawk.project.domain.config.GitCodeBaseConfig;
import com.oc.hawk.project.domain.model.codebase.CodeBase;
import com.oc.hawk.project.domain.model.codebase.git.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class RemoteGitRepository implements GitRepository {
    private final GitCodeBaseConfig gitCodeBaseConfig;

    @Override
    public List<String> loadProjectBranches(GitRepoKey key, CodeBase codeBase) {
        return getGitRepo(key, codeBase).branch();
    }

    @Override
    public void updateCodeRepo(GitRepoKey key, CodeBase codeBase) {
        getGitRepo(key, codeBase).sync();
    }


    @Override
    public GitCommitLog branchLatestCommitLog(GitRepoKey key, String branchName, CodeBase gitCodeBase) {
        final GitRepo gitRepo = getGitRepo(key, gitCodeBase);
        final RevCommit commit = gitRepo.log(branchName);
        return commitLog(commit, branchName);
    }

    public GitRepo getGitRepo(GitRepoKey key, CodeBase codeBase) {
        final String gitRepoLocalPath = getGitRepoLocalPath(key);
        final CodeBaseAuthenticator authentication = codeBase.getAuthenticator();

        CodeBaseIdentity identity = authentication == null ? null : authentication.decode();
        return new GitRepo(codeBase.url(true), identity, gitRepoLocalPath);
    }

    private String getGitRepoLocalPath(GitRepoKey key) {
        return gitCodeBaseConfig.getRepoPath() + "/" + key;
    }

    private GitCommitLog commitLog(RevCommit commit, String branch) {
        if (commit == null) {
            return null;
        }
        PersonIdent committerIdent = commit.getCommitterIdent();
        GitCommitter committer = new GitCommitter(commit.getAuthorIdent().getName(), committerIdent.getName(), committerIdent.getEmailAddress());
        return GitCommitLog.builder()
            .branch(branch)
            .committer(committer)
            .commitTime(DateUtils.dateToLocalDateTime(committerIdent.getWhen()))
            .message(commit.getFullMessage())
            .versionNumber(commit.name())
            .build();
    }

}
