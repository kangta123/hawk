package com.oc.hawk.project.domain.model.codebase;

import com.oc.hawk.project.ProjectBaseTest;
import com.oc.hawk.project.domain.model.codebase.git.GitCodeBase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class GitCodeBaseTest extends ProjectBaseTest {

    @Test
    void getUrlWithProtocol_urlWithProtocol() {
        String url = "http://github.com/architecture/demo.git";
        GitCodeBase gitCodeBase = new GitCodeBase(url, null);
        Assertions.assertThat(gitCodeBase.url(true)).isEqualTo(url);
    }
}
