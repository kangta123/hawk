package com.oc.hawk.traffic.entrypoint.domain.model.execution.request;

import com.oc.hawk.ddd.DomainValueObject;
import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@DomainValueObject
@Getter
@Builder
public class HttpUriParam {

    private final Map<String, String> params;

    public HttpUriParam(Map<String, String> params) {
        this.params = params;
    }

    public Map<String, Object> getHttpUriVariables() {
        Map<String, Object> uriVariablesMap = new HashMap<String, Object>();
        this.params.forEach((key, value) -> {
            uriVariablesMap.put(key, value);
        });
        return uriVariablesMap;
    }
}
