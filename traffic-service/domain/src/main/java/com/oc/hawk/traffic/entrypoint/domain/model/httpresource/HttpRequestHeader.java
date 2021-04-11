package com.oc.hawk.traffic.entrypoint.domain.model.httpresource;

import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HttpRequestHeader {

    private Map<String, String> headerMap;

    public HttpRequestHeader(Map<String, String> headerMap) {
        this.headerMap = headerMap;
    }
    
    public boolean isJsonContentType() {
        String contentType = this.headerMap.get("Content-Type");
        return "application/json".equalsIgnoreCase(contentType);
    }

    public boolean isFormContentType() {
        String contentType = this.headerMap.get("Content-Type");
        return "application/x-www-form-urlencoded".equalsIgnoreCase(contentType);
    }
    
}
