package com.oc.hawk.project.domain.model.codebase.git;

import com.oc.hawk.project.domain.model.codebase.git.service.GitPasswordEncryptStrategy;

/**
 * @author kangta123
 */
public class TestGitPasswordEncryptStrategy implements GitPasswordEncryptStrategy {
    @Override
    public CodeBaseIdentity getPlainText(String username, String password) {
        return new CodeBaseIdentity(username, password);
    }

    @Override
    public CodeBaseIdentity getCipherText(String username, String password) {
        return new CodeBaseIdentity(username, password);
    }
}
