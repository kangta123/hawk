package com.oc.hawk.traffic.application.entrypoint;

import com.oc.hawk.traffic.entrypoint.api.command.ExecuteCommand;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointConfig;
import com.oc.hawk.traffic.entrypoint.domain.model.execution.request.*;
import com.oc.hawk.traffic.entrypoint.domain.model.httpresource.HttpMethod;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class HttpRequestFactory {

    public HttpRequest create(EntryPointConfig entryPointConfig, ExecuteCommand executeCommand) {
        //表单提交key,value列表
        List<Map<String, String>> formsList = executeCommand.getRequestParams();
        //请求头数据key,value列表
        List<Map<String, String>> headersList = executeCommand.getRequestHeaders();
        //请求路径数据key,value列表
        List<Map<String, String>> pathsList = executeCommand.getUriParams();

        HttpMethod endPointMethodType = entryPointConfig.getHttpResource().getMethod();

        //header
        Map<String, String> headerMap = new HashMap<String, String>();
        for (Map<String, String> header : headersList) {
            String key = header.get("key");
            String value = header.get("value");
            headerMap.put(key, value);
        }

        //params
        Map<String, String> formMap = new HashMap<String, String>();
        for (Map<String, String> form : formsList) {
            String key = form.get("key");
            String value = form.get("value");
            formMap.put(key, value);
        }

        //uri
        Map<String, String> uriMap = new HashMap<String, String>();
        for (Map<String, String> uriParam : pathsList) {
            String key = uriParam.get("key");
            String value = uriParam.get("value");
            uriMap.put(key, value);
        }

        HttpBody body = null;
        HttpHeader header = new HttpHeader(headerMap);
        if (header.isJsonContentType()) {
            body = new JsonHttpBody(executeCommand.getRequestBody());
        } else if (header.isFormContentType()) {
            body = new FormHttpBody(formMap);
        }
        
        return HttpRequest.builder()
            .httpHeader(header)
            .requestMethod(HttpRequestMethod.valueOf(endPointMethodType.name()))
            .requestBody(body)
            .requestParam(new HttpRequestParam(formMap))
            .httpUriParam(new HttpUriParam(uriMap))
            .requestAddr(entryPointConfig.getHttpResource().getPath().getPath())
            .instanceId(Long.parseLong(executeCommand.getInstanceId()))
            .build();
    }


}
