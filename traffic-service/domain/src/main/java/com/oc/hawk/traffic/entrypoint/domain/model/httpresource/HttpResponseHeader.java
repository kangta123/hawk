package com.oc.hawk.traffic.entrypoint.domain.model.httpresource;

import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HttpResponseHeader {

    private final Map<String, String> responeseHeader;
    
    public HttpResponseHeader(Map<String, String> responeseHeader) {
        this.responeseHeader = responeseHeader;
    }
    
}
