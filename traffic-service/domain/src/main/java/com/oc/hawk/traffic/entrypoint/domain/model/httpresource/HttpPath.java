package com.oc.hawk.traffic.entrypoint.domain.model.httpresource;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HttpPath {

    private String path;

    public HttpPath(String path) {
        this.path = path;
    }
    
}
