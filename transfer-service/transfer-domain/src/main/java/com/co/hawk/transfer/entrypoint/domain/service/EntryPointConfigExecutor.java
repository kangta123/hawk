package com.co.hawk.transfer.entrypoint.domain.service;

import com.co.hawk.transfer.entrypoint.domain.model.request.HttpRequest;
import com.co.hawk.transfer.entrypoint.domain.model.response.HttpResponse;
import com.co.hawk.transfer.entrypoint.domain.service.excutor.EntryPointRequestExcutor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EntryPointConfigExecutor {
	
	private final EntryPointRequestExcutor executor;
	
	public HttpResponse executor(HttpRequest httpRequest) {
		HttpResponse httpResponse = executor.execute(httpRequest);
		return httpResponse;
	}
	
}
