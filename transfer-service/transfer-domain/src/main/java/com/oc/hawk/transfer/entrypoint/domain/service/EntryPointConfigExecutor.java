package com.oc.hawk.transfer.entrypoint.domain.service;

import com.oc.hawk.transfer.entrypoint.domain.model.request.HttpRequest;
import com.oc.hawk.transfer.entrypoint.domain.model.response.HttpResponse;
import com.oc.hawk.transfer.entrypoint.domain.service.excutor.EntryPointExcutor;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EntryPointConfigExecutor {
	
	private final EntryPointExcutor executor;
	
	public HttpResponse execute(HttpRequest httpRequest) {
		HttpResponse httpResponse = executor.execute(httpRequest);
		return httpResponse;
	}
}
