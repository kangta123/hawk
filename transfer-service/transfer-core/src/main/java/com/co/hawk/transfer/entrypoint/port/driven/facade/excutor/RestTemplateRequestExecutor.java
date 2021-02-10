package com.co.hawk.transfer.entrypoint.port.driven.facade.excutor;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import com.co.hawk.transfer.entrypoint.domain.model.request.HttpBody;
import com.co.hawk.transfer.entrypoint.domain.model.request.HttpHeader;
import com.co.hawk.transfer.entrypoint.domain.model.request.HttpRequest;
import com.co.hawk.transfer.entrypoint.domain.model.request.HttpRequestMethod;
import com.co.hawk.transfer.entrypoint.domain.model.request.HttpRequestParam;
import com.co.hawk.transfer.entrypoint.domain.model.request.HttpUriParam;
import com.co.hawk.transfer.entrypoint.domain.model.response.HttpResponse;
import com.co.hawk.transfer.entrypoint.domain.model.response.HttpResponseBody;
import com.co.hawk.transfer.entrypoint.domain.model.response.HttpResponseHeader;
import com.co.hawk.transfer.entrypoint.domain.service.excutor.EntryPointRequestExcutor;

@Component
public class RestTemplateRequestExecutor implements EntryPointRequestExcutor{
	
	private RestTemplate restTemplate;
	
	public RestTemplateRequestExecutor(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	@Override
	public HttpResponse execute(HttpRequest request) {
		HttpHeader header = request.getHttpHeader();
		HttpRequestParam params = request.getRequestParam();
		HttpUriParam uriParam = request.getHttpUriParam();
		HttpRequestMethod method = request.getRequestMethod();
		
		HttpResponse response = null;
		//http请求头
		HttpHeaders httpHeaders = getHttpHeaders(header.getHeaderMap());
		Map<String,Object> uriVariablesMap = getHttpUriVariables(uriParam.getParams());
		String requestUrl = getHttpRequestUrl(request.getInstanceName(),request.getRequestAddr());
		
		HttpEntity requestEntity = null;
		if(header.isJsonContentType()) {
			//json请求
			HttpBody body = request.getRequestBody();
			requestEntity = new HttpEntity<String>(body.getBody(), httpHeaders);
		}else if(header.isFormContentType()) {
			//form请求
			MultiValueMap<String, String> httpParams = getHttpParams(params.getParams());
			requestEntity = new HttpEntity<MultiValueMap<String, String>>(httpParams, httpHeaders);
		}
		
		long time = System.currentTimeMillis();
		HttpMethod httpMethod = getHttpMethod(method);
		ResponseEntity<String> responseObj = restTemplate.exchange(requestUrl, httpMethod, requestEntity, String.class, uriVariablesMap);
		time = System.currentTimeMillis() - time;
		response = getHttpResponse(responseObj.getStatusCodeValue(),
								responseObj.getBody(), 
								responseObj.getHeaders().toSingleValueMap(),
								time);
		return response;
	}
	
	private String getHttpRequestUrl(String instanceName, String path) {
		return new StringBuilder().append("http://")
								  .append(instanceName)
								  .append(":8080")
								  .append(path)
								  .toString();
	}
	
	private MultiValueMap<String, String> getHttpParams(Map<String,String> paramsMap){
		MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<String, String>();
		paramsMap.forEach((key, value) -> {
			multiValueMap.add(key, value);
		});
		return multiValueMap;
	}
	
	private HttpHeaders getHttpHeaders(Map<String,String> headersMap){
		HttpHeaders headers = new HttpHeaders();
		headersMap.forEach((key, value) -> {
			headers.set(key, value);
		});
		return headers;
	}
	
	private Map<String,Object> getHttpUriVariables(Map<String,String> uriParamsMap){
		 Map<String,Object> uriVariablesMap = new HashMap<String,Object>();
		 uriParamsMap.forEach((key, value) -> {
			 uriVariablesMap.put(key, value);
		});
		return uriVariablesMap;
	}
	
	private HttpResponse getHttpResponse(int statusCode,String responseBody,Map<String,String> responseHeadersMap,Long time) {
		return HttpResponse.builder()
				   .responseCode(String.valueOf(statusCode))
				   .responseBody(HttpResponseBody.createHttpResponseBody(responseBody))
				   .responseHeader(HttpResponseHeader.createHttpResponseHeader(responseHeadersMap))
				   .responseTime(time)
				   .build();
	}
	
	private HttpMethod getHttpMethod(HttpRequestMethod method) {
		HttpMethod httpMethod = HttpMethod.POST;
		if(method.isPost()) {
			httpMethod = HttpMethod.POST;
		}else if(method.isGet()) {
			httpMethod = HttpMethod.GET;
		}else if(method.isPut()) {
			httpMethod = HttpMethod.PUT;
		}else if(method.isDelete()) {
			httpMethod = HttpMethod.DELETE;
		}
		return httpMethod;
	}
}
