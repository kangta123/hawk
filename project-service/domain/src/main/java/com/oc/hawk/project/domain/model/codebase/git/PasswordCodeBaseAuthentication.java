package com.oc.hawk.project.domain.model.codebase.git;

import com.oc.hawk.ddd.DomainValueObject;
import com.oc.hawk.project.domain.config.GitCodeBaseConfiguration;
import com.oc.hawk.project.domain.model.codebase.git.service.AESEncryptStrategy;
import com.oc.hawk.project.domain.model.codebase.git.service.GitPasswordEncryptStrategy;

@DomainValueObject
public class PasswordCodeBaseAuthentication implements CodeBaseAuthentication {
    private String password;
    private String username;
    private GitPasswordEncryptStrategy strategy;

    public PasswordCodeBaseAuthentication(String username, String password) {
        this.username = username;
        this.password = password;
        this.strategy = new AESEncryptStrategy();
    }

    public static CodeBaseAuthentication globalGitAuthentication(GitCodeBaseConfiguration gitCodeBaseConfiguration) {
        return new PasswordCodeBaseAuthentication(gitCodeBaseConfiguration.getDefaultUsername(), gitCodeBaseConfiguration.getDefaultPassword());
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
