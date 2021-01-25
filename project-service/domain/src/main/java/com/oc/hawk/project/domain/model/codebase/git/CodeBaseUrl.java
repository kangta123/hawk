package com.oc.hawk.project.domain.model.codebase.git;

import com.oc.hawk.project.domain.model.project.URLProtocol;
import org.apache.commons.lang3.StringUtils;

public class CodeBaseUrl {
    public static final String PROTOCAL_SIGN = "://";
    private String url;
    private URLProtocol protocol;

    public CodeBaseUrl(String url) {
        if (url.contains(PROTOCAL_SIGN)) {
            String protocol = StringUtils.substringBefore(url, ":");
            this.protocol = URLProtocol.valueOf(protocol.toLowerCase());
            this.url = StringUtils.substringAfterLast(url, PROTOCAL_SIGN);
        } else {
            this.protocol = URLProtocol.http;
            this.url = url;
        }

    }

    public URLProtocol protocol() {
        return protocol;
    }

    public String url(boolean protocol) {
        if (protocol) {
            return this.protocol.withUrl(url);
        } else {
            return this.url;
        }
    }
}
