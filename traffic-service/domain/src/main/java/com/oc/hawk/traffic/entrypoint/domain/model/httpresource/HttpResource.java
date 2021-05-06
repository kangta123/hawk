package com.oc.hawk.traffic.entrypoint.domain.model.httpresource;

import lombok.Getter;

@Getter
public class HttpResource {
    
    //请求路径
    private HttpPath path;
    //请求方法
    private final HttpMethod method;
    
    public HttpResource(HttpPath path,HttpMethod method) {
        this.path=path;
        this.method=method;
    }
    
    public void updateHttpPath(HttpPath path) {
        this.path = path;
    }
    
}
