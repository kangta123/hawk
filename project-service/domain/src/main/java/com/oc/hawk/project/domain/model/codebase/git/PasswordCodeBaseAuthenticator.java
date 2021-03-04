package com.oc.hawk.project.domain.model.codebase.git;

import com.oc.hawk.ddd.DomainValueObject;
import com.oc.hawk.project.domain.model.codebase.git.service.AesEncryptStrategy;
import com.oc.hawk.project.domain.model.codebase.git.service.GitPasswordEncryptStrategy;

@DomainValueObject
public class PasswordCodeBaseAuthenticator implements CodeBaseAuthenticator {
    private final String password;
    private final String username;
    private final GitPasswordEncryptStrategy strategy;

    public PasswordCodeBaseAuthenticator(String username, String password, GitPasswordEncryptStrategy strategy) {
        this.username = username;
        this.password = password;
        this.strategy = strategy;
    }

    public PasswordCodeBaseAuthenticator(String username, String password) {
        this.username = username;
        this.password = password;
        this.strategy = new AesEncryptStrategy();
    }

    @Override
    public CodeBaseIdentity encode() {
        return strategy.getCipherText(username, password);
    }

    @Override
    public CodeBaseIdentity decode() {
        return strategy.getPlainText(username, password);
    }
}
