package com.oc.hawk.project.domain.model.codebase.git;

import com.oc.hawk.test.BaseTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author kangta123
 */
class GitCodeBaseTest extends BaseTest {

    @Test
    void urlWithAuthentication_passwordAuth() {
        final String username = str();
        final String password = str();
        final String url = str();
        final String http = "http";
        GitCodeBase gitCodeBase = new GitCodeBase(url, http, new PasswordCodeBaseAuthenticator(username, password, new TestGitPasswordEncryptStrategy()));
        Assertions.assertThat(gitCodeBase.getUrlWithAuthentication()).isEqualTo(http + "://" + username + ":" + password + "@" + url);
    }

    @Test
    void urlWithAuthentication_withoutAuth() {
        final String url = str();
        final String http = "http";
        GitCodeBase gitCodeBase = new GitCodeBase(url, http, null);
        Assertions.assertThat(gitCodeBase.getUrlWithAuthentication()).isEqualTo(http + "://" + url);
    }
}
