package com.oc.hawk.traffic.entrypoint.domain.model.httpresource;

import lombok.Getter;

@Getter
public class HttpRequestBody {

    private String body;
    
    public HttpRequestBody(String body) {
        this.body = body;
    }
}
