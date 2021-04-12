package com.oc.hawk.traffic.port.driven.facade.excutor;

import java.net.URI;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class RestTemplateProxy extends RestTemplate{

    @Override
    protected <T> T doExecute(URI url, HttpMethod method, RequestCallback requestCallback, ResponseExtractor<T> responseExtractor) throws RestClientException {
        T t = super.doExecute(url, method, requestCallback, responseExtractor);
        return t;
    }
}
