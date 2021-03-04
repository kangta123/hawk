package com.oc.hawk.transfer.entrypoint.domain.service.excutor;

import com.oc.hawk.transfer.entrypoint.domain.model.request.HttpRequest;
import com.oc.hawk.transfer.entrypoint.domain.model.response.HttpResponse;

public interface EntryPointExcutor {
	HttpResponse execute(HttpRequest request);
}
