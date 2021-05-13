package com.oc.hawk.traffic.port.driven.facade.excutor;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class CustomInterceptor implements ClientHttpRequestInterceptor{

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {
        HttpHeaders headers = request.getHeaders();
        
        List<String> contentTypeList = headers.get(HttpHeaders.CONTENT_TYPE);
        List<String> filterList = contentTypeList.stream().filter(item -> !StringUtils.equalsIgnoreCase(item, "text/plain;charset=ISO-8859-1")).collect(Collectors.toList());
        headers.remove(HttpHeaders.CONTENT_TYPE);
        if(Objects.nonNull(filterList) && !filterList.isEmpty()) {   
            headers.addAll(HttpHeaders.CONTENT_TYPE, filterList);
        }
        return execution.execute(request, body);
    }

}
