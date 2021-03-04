package com.oc.hawk.project.domain.model.codebase.git;

import com.oc.hawk.ddd.AggregateRoot;
import com.oc.hawk.project.domain.model.codebase.CodeBase;
import lombok.Getter;

import java.util.List;

@AggregateRoot
@Getter
public class GitCodeBase extends CodeBase {
    public GitCodeBase(String url, CodeBaseAuthenticator authentication) {
        super(new CodeBaseUrl(url), authentication);
    }

    public GitCodeBase(String url, String protocol, CodeBaseAuthenticator authentication) {
        super(new CodeBaseUrl(url, protocol), authentication);
    }

    public List<String> loadGitBranches(GitRepoKey key, GitRepository gitRepository) {
        return gitRepository.loadProjectBranches(key, this);
    }


    @Override
    public String getUrlWithAuthentication() {
        final CodeBaseAuthenticator authenticator = getAuthenticator();
        if (authenticator == null) {
            return url.url(true);
        }
        CodeBaseIdentity codeBaseIdentity = authenticator.decode();
        return url.protocol(true) + codeBaseIdentity.getUsername() + ":" + codeBaseIdentity.getKey() + "@" + url.url(false);
    }

    public String urlProtocol() {
        return String.valueOf(this.url.protocol());
    }


    public GitCommitLog getLatestCommitLog(GitRepoKey key, String branch, GitRepository gitRepository) {
        return gitRepository.branchLatestCommitLog(key, branch, this);
    }
}
