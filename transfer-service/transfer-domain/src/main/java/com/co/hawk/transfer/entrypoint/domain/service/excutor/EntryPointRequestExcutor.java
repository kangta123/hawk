package com.co.hawk.transfer.entrypoint.domain.service.excutor;

import com.co.hawk.transfer.entrypoint.domain.model.request.HttpRequest;
import com.co.hawk.transfer.entrypoint.domain.model.response.HttpResponse;

public interface EntryPointRequestExcutor {
	HttpResponse execute(HttpRequest request);
}
