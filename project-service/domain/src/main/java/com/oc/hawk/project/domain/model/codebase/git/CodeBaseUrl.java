package com.oc.hawk.project.domain.model.codebase.git;

import org.apache.commons.lang3.StringUtils;

public class CodeBaseUrl {
    public static final String PROTOCOL_SIGN = "://";
    private final String url;
    private final String protocol;

    public CodeBaseUrl(String url, String protocol) {

        this(protocol + PROTOCOL_SIGN + url);
    }

    public CodeBaseUrl(String url) {
        if (url.contains(PROTOCOL_SIGN)) {
            String protocol = StringUtils.substringBefore(url, PROTOCOL_SIGN);
            this.protocol = protocol.toLowerCase();
            this.url = StringUtils.substringAfterLast(url, PROTOCOL_SIGN);
        } else {
            this.protocol = "";
            this.url = url;
        }

    }

    public String protocol() {
        return protocol(false);
    }

    public String protocol(boolean sign) {
        if (sign) {
            return protocol + PROTOCOL_SIGN;
        }
        return protocol;
    }

    public String url(boolean protocol) {
        if (protocol) {
            if (StringUtils.isEmpty(this.protocol)) {
                return url;
            } else {
                return this.protocol + PROTOCOL_SIGN + url;
            }
        } else {
            return this.url;
        }
    }
}
