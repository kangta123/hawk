package com.oc.hawk.project.application;

import com.oc.hawk.project.api.command.RegisterProjectCommand;
import com.oc.hawk.project.domain.config.GitCodeBaseConfiguration;
import com.oc.hawk.project.domain.model.codebase.CodeBase;
import com.oc.hawk.project.domain.model.codebase.git.CodeBaseAuthentication;
import com.oc.hawk.project.domain.model.codebase.git.GitCodeBase;
import com.oc.hawk.project.domain.model.codebase.git.PasswordCodeBaseAuthentication;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CodeBaseFactory {
    private final GitCodeBaseConfiguration gitCodeBaseConfiguration;

    public CodeBase create(RegisterProjectCommand command) {
        CodeBaseAuthentication authentication;
        if (StringUtils.isNotEmpty(command.getPassword()) && StringUtils.isNotEmpty(command.getUsername())) {
            return new GitCodeBase(command.getUrl(), new PasswordCodeBaseAuthentication(command.getUsername(), command.getPassword()));
        } else {
            authentication = PasswordCodeBaseAuthentication.globalGitAuthentication(gitCodeBaseConfiguration);
        }
        return new GitCodeBase(command.getUrl(), authentication);
    }

}
