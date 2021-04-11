package com.oc.hawk.traffic.entrypoint.domain.model.httpresource;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HttpResponseBody {
    
    private final String body;

    public HttpResponseBody(String body) {
        this.body=body;
    }
    
    public static HttpResponseBody createHttpResponseBody(String body) {
        return HttpResponseBody.builder().body(body).build();
    }
    
}
