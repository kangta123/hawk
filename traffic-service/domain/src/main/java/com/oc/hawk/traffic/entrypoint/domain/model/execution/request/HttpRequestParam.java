package com.oc.hawk.traffic.entrypoint.domain.model.execution.request;

import com.oc.hawk.ddd.DomainValueObject;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@DomainValueObject
@Getter
@Builder
public class HttpRequestParam {

    private final Map<String, String> params;

    public HttpRequestParam(Map<String, String> params) {
        this.params = params;
    }

    public boolean nonNull() {
        return params != null && !params.isEmpty();
    }
}
