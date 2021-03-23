package com.oc.hawk.traffic.entrypoint.domain.model.execution.response;

import com.oc.hawk.ddd.DomainValueObject;
import lombok.Builder;
import lombok.Getter;

@DomainValueObject
@Getter
@Builder
public class HttpResponseBody {

    private final String body;

    public static HttpResponseBody createHttpResponseBody(String body) {
        return HttpResponseBody.builder().body(body).build();
    }

}
