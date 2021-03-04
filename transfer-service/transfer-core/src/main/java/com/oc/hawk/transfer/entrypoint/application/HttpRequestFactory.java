package com.oc.hawk.transfer.entrypoint.application;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

import com.oc.hawk.transfer.entrypoint.api.command.ExecuteCommand;
import com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointConfig;
import com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointMethod;
import com.oc.hawk.transfer.entrypoint.domain.model.request.FormHttpBody;
import com.oc.hawk.transfer.entrypoint.domain.model.request.HttpBody;
import com.oc.hawk.transfer.entrypoint.domain.model.request.HttpHeader;
import com.oc.hawk.transfer.entrypoint.domain.model.request.HttpRequest;
import com.oc.hawk.transfer.entrypoint.domain.model.request.HttpRequestMethod;
import com.oc.hawk.transfer.entrypoint.domain.model.request.HttpRequestParam;
import com.oc.hawk.transfer.entrypoint.domain.model.request.HttpUriParam;
import com.oc.hawk.transfer.entrypoint.domain.model.request.JsonHttpBody;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class HttpRequestFactory {
	
	public HttpRequest create(EntryPointConfig entryPointConfig, ExecuteCommand executeCommand) {
		//表单提交key,value列表
		List<Map<String, String>> formsList = executeCommand.getForm();
		//请求头数据key,value列表
		List<Map<String, String>> headersList = executeCommand.getHeader();
		//请求路径数据key,value列表
		List<Map<String, String>> pathsList = executeCommand.getPath();
		
		EntryPointMethod endPointMethodType = entryPointConfig.getHttpResource().getMethod();
		
		//header
		Map<String,String> headerMap = new HashMap<String,String>();
		for(Map<String, String> header : headersList) {
			String key = header.get("key");
			String value = header.get("value");
			headerMap.put(key, value);
		}
		
		//params
		Map<String,String> formMap = new HashMap<String,String>();
		for(Map<String, String> form : formsList) {
			String key = form.get("key");
			String value = form.get("value");
			formMap.put(key, value);
		}
		
		//uri
		Map<String,String> uriMap = new HashMap<String,String>();
		for(Map<String, String> uriParam : pathsList) {
			String key = uriParam.get("key");
			String value = uriParam.get("value");
			uriMap.put(key, value);
		}
		
		HttpBody body = null;
		HttpHeader header = new HttpHeader(headerMap);
		if(header.isJsonContentType()) {
			body = new JsonHttpBody(executeCommand.getBody());
		}else if(header.isFormContentType()) {
			body = new FormHttpBody(formMap);
		}
		
		return HttpRequest.builder()
					.httpHeader(header)
					.requestMethod(HttpRequestMethod.valueOf(endPointMethodType.name()))
					.requestBody(body)
					.requestParam(new HttpRequestParam(formMap))
					.httpUriParam(new HttpUriParam(uriMap))
					.requestAddr(entryPointConfig.getHttpResource().getPath().getPath())
					.instanceName(executeCommand.getInstanceName())
					.build();
	}
	
	
}
