package com.oc.hawk.project.port.driven.persistence.po;

import com.oc.hawk.common.hibernate.BaseEntity;
import com.oc.hawk.project.domain.model.codebase.CodeBase;
import com.oc.hawk.project.domain.model.codebase.git.*;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "project_git_codebase")
@Getter
@Setter
public class GitCodeBasePO extends BaseEntity {
    private String url;
    private String protocol;
    private String username;
    private String password;
    private String token;


    public static GitCodeBasePO createdBy(GitCodeBase gitCodeBase) {

        GitCodeBasePO gitCodeBasePo = new GitCodeBasePO();

        gitCodeBasePo.setProtocol(gitCodeBase.urlProtocol());

        CodeBaseAuthenticator authentication = gitCodeBase.getAuthenticator();
        if (authentication != null) {
            CodeBaseIdentity codeBaseIdentity = authentication.encode();
            if (authentication instanceof PasswordCodeBaseAuthenticator) {
                gitCodeBasePo.setPassword(codeBaseIdentity.getKey());
            }
            if (authentication instanceof TokenCodeBaseAuthenticator) {
                gitCodeBasePo.setToken(codeBaseIdentity.getKey());
            }
            gitCodeBasePo.setUsername(codeBaseIdentity.getUsername());
        }

        gitCodeBasePo.setUrl(gitCodeBase.url(false));
        return gitCodeBasePo;
    }


    public CodeBase toGitCodeBase() {
        CodeBaseAuthenticator authentication = null;
        if (StringUtils.isNotEmpty(this.password)) {
            authentication = new PasswordCodeBaseAuthenticator(username, password);
        } else if (StringUtils.isNotEmpty(this.token)) {
            authentication = new TokenCodeBaseAuthenticator(username, token);
        }

        return new GitCodeBase(url, protocol, authentication);
    }
}
