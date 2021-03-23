package com.oc.hawk.traffic.entrypoint.domain.model.execution.response;

import com.oc.hawk.ddd.DomainValueObject;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@DomainValueObject
@Getter
@Builder
public class HttpResponseHeader {

    private final Map<String, String> responeseHeader;

    public static HttpResponseHeader createHttpResponseHeader(Map<String, String> responseHeader) {
        return HttpResponseHeader.builder().responeseHeader(responseHeader).build();
    }
}
