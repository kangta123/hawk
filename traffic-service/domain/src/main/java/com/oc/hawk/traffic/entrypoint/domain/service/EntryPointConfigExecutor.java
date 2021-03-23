package com.oc.hawk.traffic.entrypoint.domain.service;

import com.oc.hawk.traffic.entrypoint.domain.model.execution.request.HttpRequest;
import com.oc.hawk.traffic.entrypoint.domain.model.execution.response.HttpResponse;
import com.oc.hawk.traffic.entrypoint.domain.service.excutor.EntryPointExcutor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EntryPointConfigExecutor {

    private final EntryPointExcutor executor;

    public HttpResponse execute(HttpRequest httpRequest) {
        HttpResponse httpResponse = executor.execute(httpRequest);
        return httpResponse;
    }
}
