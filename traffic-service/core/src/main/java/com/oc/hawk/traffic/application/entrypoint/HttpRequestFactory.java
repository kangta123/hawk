package com.oc.hawk.traffic.application.entrypoint;

import com.oc.hawk.traffic.entrypoint.api.command.ExecuteCommand;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointConfig;
import com.oc.hawk.traffic.entrypoint.domain.model.execution.request.*;
import com.oc.hawk.traffic.entrypoint.domain.model.httpresource.HttpMethod;
import com.oc.hawk.traffic.entrypoint.domain.model.httpresource.HttpRequestHeader;

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
        Map<String, String> headerMap = getMap(headersList);
        //params
        Map<String, String> formMap = getMap(formsList);
        //uri
        Map<String, String> uriMap = getMap(pathsList);
        
        HttpBody body = null;
        HttpRequestHeader header = new HttpRequestHeader(headerMap);
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
    
    private Map<String, String> getMap(List<Map<String, String>> list){
        Map<String, String> resultMap = new HashMap<>();
        for (Map<String, String> map : list) {
            String key = map.get("key");
            String value = map.get("value");
            resultMap.put(key, value);
        }
        return resultMap;
    }


}
