package com.oc.hawk.project.domain.model.codebase;

import com.oc.hawk.project.domain.model.codebase.git.CodeBaseAuthentication;
import com.oc.hawk.project.domain.model.codebase.git.CodeBaseUrl;


public abstract class CodeBase {
    protected CodeBaseID codeBaseId;
    protected CodeBaseUrl url;
    protected CodeBaseAuthentication authentication;

    public CodeBase(String url, CodeBaseAuthentication authentication) {
        this.url = new CodeBaseUrl(url);
        this.authentication = authentication;
    }

    public abstract String urlWithAuthentication();

    public CodeBaseID getCodeBaseId() {
        return codeBaseId;
    }

    public CodeBaseUrl getUrl() {
        return url;
    }

    public CodeBaseAuthentication getAuthentication() {
        return authentication;
    }

    public String url(boolean protocol) {
        return this.url.url(protocol);
    }
}
