package com.oc.hawk.traffic.entrypoint.domain.model.execution.request;

import com.oc.hawk.ddd.DomainEntity;

@DomainEntity
public abstract class HttpBody {

    Object body;

    public HttpBody(Object body) {
        this.body = body;
    }

    public abstract Object getData();
}
