package com.oc.hawk.project.domain.model.codebase.git;

import com.oc.hawk.test.BaseTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author kangta123
 */
class CodeBaseUrlTest extends BaseTest {
    @Test
    public void testProtocol() {
        final CodeBaseUrl codeBaseUrl = new CodeBaseUrl("http://testabc");
        Assertions.assertThat(codeBaseUrl.protocol()).isEqualTo("http");
    }

    @Test
    public void testUrl_urlBody() {
        final CodeBaseUrl codeBaseUrl = new CodeBaseUrl("http://testabcss");
        Assertions.assertThat(codeBaseUrl.url(false)).isEqualTo("testabcss");
    }

    @Test
    public void testUrl_fullUrl() {
        final String url = "http://testabcss";
        final CodeBaseUrl codeBaseUrl = new CodeBaseUrl(url);
        Assertions.assertThat(codeBaseUrl.url(true)).isEqualTo(url);
    }

    @Test
    public void testUrl_fullUrlProtocolEmpty() {
        final String url = "git@gitlab.cn:test/demo.git";
        final CodeBaseUrl codeBaseUrl = new CodeBaseUrl(url);
        Assertions.assertThat(codeBaseUrl.url(true)).isEqualTo(url);
    }
}
