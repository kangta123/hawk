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
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RestTemplateRequestExecutor implements EntryPointExcutor {

    private final RestTemplate customRestTemplate;
    private final ContainerFacade containerFacade;

    public RestTemplateRequestExecutor(RestTemplate customRestTemplate, ContainerFacade containerFacade) {
        this.customRestTemplate = customRestTemplate;
        this.containerFacade = containerFacade;
    }

    @Override
    public HttpResponse execute(HttpRequest request) {
        HttpHeaders httpHeaders = getHttpHeaders(request.getHttpHeader().getHeaderMap());
        Map<String, Object> uriVariablesMap = request.getHttpUriParam().getHttpUriVariables();

        InstanceConfigDTO instanceConfigDTO = containerFacade.getById(request.getInstanceId());

        String requestUrl = request.getHttpRequestUrl(instanceConfigDTO.getServiceName(), instanceConfigDTO.getNamespace());
        StringBuilder requestUrlBuilder = new StringBuilder(requestUrl);
        HttpEntity requestEntity = getHttpEntity(request, httpHeaders, requestUrlBuilder);

        Stopwatch stopWatch = Stopwatch.createStarted();
        HttpMethod httpMethod = getHttpMethod(request.getRequestMethod());
        ResponseEntity<String> responseObj = customRestTemplate.exchange(requestUrlBuilder.toString(), httpMethod, requestEntity, String.class, uriVariablesMap);
        stopWatch.stop();
        return HttpResponse.builder()
                .responseCode(String.valueOf(responseObj.getStatusCodeValue()))
                .responseBody(HttpResponseBody.createHttpResponseBody(responseObj.getBody()))
                .responseHeader(HttpResponseHeader.createHttpResponseHeader(responseObj.getHeaders().toSingleValueMap()))
                .responseTime(stopWatch.elapsed(TimeUnit.MILLISECONDS))
                .build();
    }

    private HttpEntity getHttpEntity(HttpRequest request, HttpHeaders httpHeaders, StringBuilder requestUrlBuilder) {
        if (request.getRequestBody() instanceof JsonHttpBody) {
            return new HttpEntity<>(((JsonHttpBody) request.getRequestBody()).getData(), httpHeaders);
        } else if (request.getRequestBody() instanceof FormHttpBody) {
            Map<String, String> paramsMap = request.getRequestParam().getParams();
            if (Objects.nonNull(paramsMap)) {
                StringBuilder sb = new StringBuilder("?");
                Object[] keyArray = paramsMap.keySet().toArray();
                for (int i = 0; i < keyArray.length; i++) {
                    String paramKey = (String) keyArray[i];
                    String paramValue = paramsMap.get(paramKey);
                    if (i == keyArray.length - 1) {
                        sb.append(paramKey).append("=").append(paramValue);
                    } else {
                        sb.append(paramKey).append("=").append(paramValue).append("&");
                    }
                }
                requestUrlBuilder.append(sb);
            }
            return new HttpEntity<>(((FormHttpBody) request.getRequestBody()).getData(), httpHeaders);
        }
        return null;
    }

    private HttpHeaders getHttpHeaders(Map<String, String> headersMap) {
        HttpHeaders headers = new HttpHeaders();
        headersMap.forEach(headers::set);
        return headers;
    }

    private HttpMethod getHttpMethod(HttpRequestMethod method) {
        return HttpMethod.resolve(method.name());
    }
}
