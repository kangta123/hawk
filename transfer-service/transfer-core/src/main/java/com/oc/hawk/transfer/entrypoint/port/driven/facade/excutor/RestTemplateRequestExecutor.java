package com.oc.hawk.transfer.entrypoint.port.driven.facade.excutor;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Stopwatch;
import com.oc.hawk.transfer.entrypoint.domain.model.request.FormHttpBody;
import com.oc.hawk.transfer.entrypoint.domain.model.request.HttpBody;
import com.oc.hawk.transfer.entrypoint.domain.model.request.HttpHeader;
import com.oc.hawk.transfer.entrypoint.domain.model.request.HttpRequest;
import com.oc.hawk.transfer.entrypoint.domain.model.request.HttpRequestMethod;
import com.oc.hawk.transfer.entrypoint.domain.model.request.HttpRequestParam;
import com.oc.hawk.transfer.entrypoint.domain.model.request.HttpUriParam;
import com.oc.hawk.transfer.entrypoint.domain.model.request.JsonHttpBody;
import com.oc.hawk.transfer.entrypoint.domain.model.response.HttpResponse;
import com.oc.hawk.transfer.entrypoint.domain.model.response.HttpResponseBody;
import com.oc.hawk.transfer.entrypoint.domain.model.response.HttpResponseHeader;
import com.oc.hawk.transfer.entrypoint.domain.service.excutor.EntryPointExcutor;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RestTemplateRequestExecutor implements EntryPointExcutor{
	
	private RestTemplate restTemplate;
		
	public RestTemplateRequestExecutor(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	@Override
	public HttpResponse execute(HttpRequest request) {		
		HttpHeaders httpHeaders = getHttpHeaders(request.getHttpHeader().getHeaderMap());
		Map<String,Object> uriVariablesMap = request.getHttpUriParam().getHttpUriVariables();
		String requestUrl = request.getHttpRequestUrl();
		HttpEntity requestEntity = getHttpEntity(request,httpHeaders);
		
		Stopwatch stopWatch = Stopwatch.createStarted();
		HttpMethod httpMethod = getHttpMethod(request.getRequestMethod());
		ResponseEntity<String> responseObj = restTemplate.exchange(requestUrl, httpMethod, requestEntity, String.class, uriVariablesMap);
		stopWatch.stop();
		return HttpResponse.builder()
				   .responseCode(String.valueOf(responseObj.getStatusCodeValue()))
				   .responseBody(HttpResponseBody.createHttpResponseBody(responseObj.getBody()))
				   .responseHeader(HttpResponseHeader.createHttpResponseHeader(responseObj.getHeaders().toSingleValueMap()))
				   .responseTime(stopWatch.elapsed(TimeUnit.MILLISECONDS))
				   .build();
	}
	
	private HttpEntity<Object> getHttpEntity(HttpRequest request,HttpHeaders httpHeaders){
		HttpEntity entity = null;
		if(request.getRequestBody() instanceof JsonHttpBody) {
			entity = new HttpEntity<String>(((JsonHttpBody)request.getRequestBody()).getData(), httpHeaders);
		}else if(request.getRequestBody() instanceof FormHttpBody) {
			MultiValueMap<String, String> httpParams = getHttpParams(((FormHttpBody)request.getRequestBody()).getData());
			entity = new HttpEntity<MultiValueMap<String, String>>(httpParams, httpHeaders);
		}
		return entity;
	}
	
	private HttpHeaders getHttpHeaders(Map<String,String> headersMap){
		HttpHeaders headers = new HttpHeaders();
		headersMap.forEach(headers::set);
		return headers;
	}
	
	private MultiValueMap<String, String> getHttpParams(Map<String,String> paramsMap){
		MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<String, String>();
		paramsMap.forEach(multiValueMap::add);
		return multiValueMap;
	}
	
	private HttpMethod getHttpMethod(HttpRequestMethod method) {
		return HttpMethod.resolve(method.name());
	}
}
