package com.oc.hawk.project.domain.model.codebase.git;

import com.oc.hawk.ddd.DomainValueObject;
import com.oc.hawk.project.domain.config.GitCodeBaseConfig;
import com.oc.hawk.project.domain.model.codebase.git.service.AesEncryptStrategy;
import com.oc.hawk.project.domain.model.codebase.git.service.GitPasswordEncryptStrategy;

@DomainValueObject
public class PasswordCodeBaseAuthentication implements CodeBaseAuthentication {
    private final String password;
    private final String username;
    private final GitPasswordEncryptStrategy strategy;

    public PasswordCodeBaseAuthentication(String username, String password) {
        this.username = username;
        this.password = password;
        this.strategy = new AesEncryptStrategy();
    }

    public static CodeBaseAuthentication globalGitAuthentication(GitCodeBaseConfig gitCodeBaseConfig) {
        return new PasswordCodeBaseAuthentication(gitCodeBaseConfig.getDefaultUsername(), gitCodeBaseConfig.getDefaultPassword());
    }


    @Override
    public PasswordAuthentication encode() {
        return strategy.getCipherText(username, password);
    }

    @Override
    public PasswordAuthentication decode() {
        return strategy.getPlainText(username, password);
    }
}
