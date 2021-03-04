package com.oc.hawk.project.domain.model.codebase;

import com.oc.hawk.project.domain.model.codebase.git.CodeBaseAuthenticator;
import com.oc.hawk.project.domain.model.codebase.git.CodeBaseUrl;


public abstract class CodeBase {
    protected CodeBaseID codeBaseId;
    protected CodeBaseUrl url;
    protected CodeBaseAuthenticator authenticator;

    public CodeBase(CodeBaseUrl url, CodeBaseAuthenticator authenticator) {
        this.url = url;
        this.authenticator = authenticator;
    }

    public abstract String getUrlWithAuthentication();

    public CodeBaseUrl getUrl() {
        return url;
    }

    public CodeBaseAuthenticator getAuthenticator() {
        return authenticator;
    }

    public String url(boolean protocol) {
        return this.url.url(protocol);
    }
}
