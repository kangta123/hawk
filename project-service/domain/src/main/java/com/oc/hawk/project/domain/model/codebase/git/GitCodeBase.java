package com.oc.hawk.project.domain.model.codebase.git;

import com.oc.hawk.ddd.AggregateRoot;
import com.oc.hawk.project.domain.model.codebase.CodeBase;
import lombok.Getter;

import java.util.List;

@AggregateRoot
@Getter
public class GitCodeBase extends CodeBase {
    public GitCodeBase(String url, CodeBaseAuthentication authentication) {
        super(url, authentication);
    }

    public List<String> loadGitBranches(GitRepoKey key, GitRepository gitRepository) {
        return gitRepository.loadProjectBranches(key, this);
    }


    @Override
    public String urlWithAuthentication() {
        PasswordAuthentication passwordAuthentication = getAuthentication().decode();
        return "http://" + passwordAuthentication.getUsername() + ":" + passwordAuthentication.getKey() + "@" + url.url(false);
    }

    public String urlProtocol() {
        return String.valueOf(this.url.protocol());
    }


    public GitCommitLog getLatestCommitLog(GitRepoKey key, String branch, GitRepository gitRepository) {
        return gitRepository.branchLatestCommitLog(key, branch, this);
    }
}
