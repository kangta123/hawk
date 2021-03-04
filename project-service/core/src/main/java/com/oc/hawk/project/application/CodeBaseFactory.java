package com.oc.hawk.project.application;

import com.oc.hawk.project.api.command.RegisterProjectCommand;
import com.oc.hawk.project.domain.config.GitCodeBaseConfig;
import com.oc.hawk.project.domain.model.codebase.CodeBase;
import com.oc.hawk.project.domain.model.codebase.git.CodeBaseAuthenticator;
import com.oc.hawk.project.domain.model.codebase.git.GitCodeBase;
import com.oc.hawk.project.domain.model.codebase.git.PasswordCodeBaseAuthenticator;
import com.oc.hawk.project.domain.model.codebase.git.TokenCodeBaseAuthenticator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CodeBaseFactory {
    private final GitCodeBaseConfig gitCodeBaseConfig;

    public CodeBase create(RegisterProjectCommand command) {
        CodeBaseAuthenticator authenticator;
        if (StringUtils.isNotEmpty(command.getToken())) {
            authenticator = new TokenCodeBaseAuthenticator(command.getToken(), command.getUsername());
        } else if (StringUtils.isNotEmpty(command.getPassword()) && StringUtils.isNotEmpty(command.getUsername())) {
            authenticator = new PasswordCodeBaseAuthenticator(command.getUsername(), command.getPassword());
        } else {
            authenticator = null;
        }
        return new GitCodeBase(command.getUrl(), authenticator);
    }

}
