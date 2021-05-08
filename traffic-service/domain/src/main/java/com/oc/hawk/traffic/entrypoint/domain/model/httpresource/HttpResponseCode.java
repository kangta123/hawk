package com.oc.hawk.traffic.entrypoint.domain.model.httpresource;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public class HttpResponseCode {
    private static final HttpResponseCode NULL = new HttpResponseCode(-1);

    private final int code;

    public HttpResponseCode(int responseCode) {
        this.code = responseCode;
    }

    public static HttpResponseCode of(String code){
        if(StringUtils.isEmpty(code)){
            return NULL;
        }
        return new HttpResponseCode(Integer.parseInt(code));
    }

}
