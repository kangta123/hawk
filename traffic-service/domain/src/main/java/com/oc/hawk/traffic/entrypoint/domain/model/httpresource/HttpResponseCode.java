package com.oc.hawk.traffic.entrypoint.domain.model.httpresource;

import lombok.Getter;

@Getter
public class HttpResponseCode {

    private Integer code;
    
    public HttpResponseCode(Integer responseCode) {
        this.code = responseCode;
    }
    
}
