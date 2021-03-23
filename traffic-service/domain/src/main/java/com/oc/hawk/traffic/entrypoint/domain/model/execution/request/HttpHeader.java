package com.oc.hawk.traffic.entrypoint.domain.model.execution.request;

import com.oc.hawk.ddd.DomainValueObject;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@DomainValueObject
@Getter
@NoArgsConstructor
public class HttpHeader {

    private Map<String, String> headerMap;

    public HttpHeader(Map<String, String> headerMap) {
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
