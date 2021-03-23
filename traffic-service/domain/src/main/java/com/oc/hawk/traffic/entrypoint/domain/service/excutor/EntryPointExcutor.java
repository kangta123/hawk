package com.oc.hawk.traffic.entrypoint.domain.service.excutor;

import com.oc.hawk.traffic.entrypoint.domain.model.execution.request.HttpRequest;
import com.oc.hawk.traffic.entrypoint.domain.model.execution.response.HttpResponse;

public interface EntryPointExcutor {
    HttpResponse execute(HttpRequest request);
}
