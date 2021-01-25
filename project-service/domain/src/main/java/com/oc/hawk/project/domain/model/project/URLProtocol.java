package com.oc.hawk.project.domain.model.project;

public enum URLProtocol {
    http, https;

    public String withUrl(String url) {
        return this + "://" + url;
    }
}
