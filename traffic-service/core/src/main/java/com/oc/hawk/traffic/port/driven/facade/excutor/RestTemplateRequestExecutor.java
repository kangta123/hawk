package com.oc.hawk.traffic.port.driven.facade.excutor;

import com.google.common.base.Stopwatch;
import com.oc.hawk.container.api.dto.InstanceConfigDTO;
import com.oc.hawk.traffic.application.entrypoint.representation.facade.ContainerFacade;
import com.oc.hawk.traffic.entrypoint.domain.model.execution.request.FormHttpBody;
import com.oc.hawk.traffic.entrypoint.domain.model.execution.request.HttpRequest;
import com.oc.hawk.traffic.entrypoint.domain.model.execution.request.HttpRequestMethod;
import com.oc.hawk.traffic.entrypoint.domain.model.execution.request.JsonHttpBody;
import com.oc.hawk.traffic.entrypoint.domain.model.execution.response.HttpResponse;
import com.oc.hawk.traffic.entrypoint.domain.model.execution.response.HttpResponseBody;
import com.oc.hawk.traffic.entrypoint.domain.model.execution.response.HttpResponseHeader;
import com.oc.hawk.traffic.entrypoint.domain.service.excutor.EntryPointExcutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RestTemplateRequestExecutor implements EntryPointExcutor {

    private final RestTemplate customRestTemplate;
    private final ContainerFacade containerFacade;

    public RestTemplateRequestExecutor(RestTemplate customRestTemplate,ContainerFacade containerFacade) {
        this.customRestTemplate = customRestTemplate;
        this.containerFacade = containerFacade;
    }

    @Override
    public HttpResponse execute(HttpRequest request) {
        HttpHeaders httpHeaders = getHttpHeaders(request.getHttpHeader().getHeaderMap());
        Map<String, Object> uriVariablesMap = request.getHttpUriParam().getHttpUriVariables();
        
        InstanceConfigDTO instanceConfigDTO = containerFacade.getById(request.getInstanceId());
        
        String requestUrl = request.getHttpRequestUrl(instanceConfigDTO.getServiceName(),instanceConfigDTO.getNamespace());
        HttpEntity requestEntity = getHttpEntity(request, httpHeaders);

        Stopwatch stopWatch = Stopwatch.createStarted();
        HttpMethod httpMethod = getHttpMethod(request.getRequestMethod());
        ResponseEntity<String> responseObj = customRestTemplate.exchange(requestUrl, httpMethod, requestEntity, String.class, uriVariablesMap);
        stopWatch.stop();
        return HttpResponse.builder()
            .responseCode(String.valueOf(responseObj.getStatusCodeValue()))
            .responseBody(HttpResponseBody.createHttpResponseBody(responseObj.getBody()))
            .responseHeader(HttpResponseHeader.createHttpResponseHeader(responseObj.getHeaders().toSingleValueMap()))
            .responseTime(stopWatch.elapsed(TimeUnit.MILLISECONDS))
            .build();
    }

    private HttpEntity<Object> getHttpEntity(HttpRequest request, HttpHeaders httpHeaders) {
        HttpEntity entity = null;
        if (request.getRequestBody() instanceof JsonHttpBody) {
            entity = new HttpEntity<>(((JsonHttpBody) request.getRequestBody()).getData(), httpHeaders);
        } else if (request.getRequestBody() instanceof FormHttpBody) {
            MultiValueMap<String, String> httpParams = getHttpParams(((FormHttpBody) request.getRequestBody()).getData());
            entity = new HttpEntity<>(httpParams, httpHeaders);
        }
        return entity;
    }

    private HttpHeaders getHttpHeaders(Map<String, String> headersMap) {
        HttpHeaders headers = new HttpHeaders();
        headersMap.forEach(headers::set);
        return headers;
    }

    private MultiValueMap<String, String> getHttpParams(Map<String, String> paramsMap) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        paramsMap.forEach(multiValueMap::add);
        return multiValueMap;
    }

    private HttpMethod getHttpMethod(HttpRequestMethod method) {
        return HttpMethod.resolve(method.name());
    }
}
