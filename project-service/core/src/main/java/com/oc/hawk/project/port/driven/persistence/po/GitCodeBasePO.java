package com.oc.hawk.project.port.driven.persistence.po;

import com.oc.hawk.api.exception.BaseException;
import com.oc.hawk.common.hibernate.BaseEntity;
import com.oc.hawk.project.domain.model.codebase.CodeBase;
import com.oc.hawk.project.domain.model.codebase.git.CodeBaseAuthentication;
import com.oc.hawk.project.domain.model.codebase.git.GitCodeBase;
import com.oc.hawk.project.domain.model.codebase.git.PasswordAuthentication;
import com.oc.hawk.project.domain.model.codebase.git.PasswordCodeBaseAuthentication;
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

        CodeBaseAuthentication authentication = gitCodeBase.getAuthentication();
        PasswordAuthentication passwordAuthentication = authentication.encode();
        if (authentication instanceof PasswordCodeBaseAuthentication) {
            gitCodeBasePo.setPassword(passwordAuthentication.getKey());
        }
        gitCodeBasePo.setUsername(passwordAuthentication.getUsername());

        gitCodeBasePo.setUrl(gitCodeBase.url(false));
        return gitCodeBasePo;
    }


    public CodeBase toGitCodeBase() {
        CodeBaseAuthentication authentication = null;
        if (StringUtils.isNotEmpty(this.password)) {
            authentication = new PasswordCodeBaseAuthentication(username, password);
        }

        if (authentication == null) {
            throw new BaseException("不支持此验证类型");
        }

        return new GitCodeBase(url, authentication);
    }
}
