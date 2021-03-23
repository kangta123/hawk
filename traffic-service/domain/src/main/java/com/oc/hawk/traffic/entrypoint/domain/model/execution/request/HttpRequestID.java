package com.oc.hawk.traffic.entrypoint.domain.model.execution.request;

import com.oc.hawk.ddd.DomainValueObject;
import lombok.Getter;
import lombok.NoArgsConstructor;

@DomainValueObject
@Getter
@NoArgsConstructor
public class HttpRequestID {

    private Long id;

    public HttpRequestID(Long id) {
        this.id = id;
    }

}
