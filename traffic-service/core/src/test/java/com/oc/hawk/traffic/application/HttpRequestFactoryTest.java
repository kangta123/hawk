package com.oc.hawk.traffic.application;

import com.oc.hawk.traffic.application.entrypoint.HttpRequestFactory;
import com.oc.hawk.traffic.entrypoint.api.command.ExecuteCommand;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.*;
import com.oc.hawk.traffic.entrypoint.domain.model.execution.request.HttpRequest;
import com.oc.hawk.traffic.entrypoint.domain.model.httpresource.HttpMethod;
import com.oc.hawk.traffic.entrypoint.domain.model.httpresource.HttpPath;
import com.oc.hawk.traffic.entrypoint.domain.model.httpresource.HttpResource;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequestFactoryTest extends TrafficBaseTest {

    HttpRequestFactory httpRequestFactory;

    @BeforeEach
    public void setup() {
        httpRequestFactory = new HttpRequestFactory();
    }

    @Test
    public void create_httpRequest() {
        EntryPointConfig entryPointConfig = getEntryPointConfig();
        ExecuteCommand executeCommand = getExecuteCommand();

        HttpRequest httpRequest = httpRequestFactory.create(entryPointConfig, executeCommand);
        Assertions.assertThat(httpRequest).isNotNull();
    }

    private EntryPointConfig getEntryPointConfig() {
        return EntryPointConfig.builder()
            .configId(new EntryPointConfigID(along()))
            .description(new EntryPointDescription(str(), str()))
            .groupId(new EntryPointGroupID(along()))
            .httpResource(new HttpResource(new HttpPath(str()), HttpMethod.valueOf("POST")))
            .build();
    }

    private ExecuteCommand getExecuteCommand() {
        ExecuteCommand command = new ExecuteCommand();
        command.setRequestBody(str());
        command.setEntryPointId(along());
        command.setInstanceId("1");

        List<Map<String, String>> formList = new ArrayList<>();
        Map<String, String> formMap = new HashMap<>();
        formMap.put("key", str());
        formMap.put("value", str());
        formList.add(formMap);
        command.setRequestParams(formList);

        List<Map<String, String>> headerList = new ArrayList<>();
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("key", str());
        headerMap.put("value", str());
        command.setRequestHeaders(headerList);

        List<Map<String, String>> pathList = new ArrayList<>();
        Map<String, String> pathMap = new HashMap<>();
        pathMap.put("key", str());
        pathMap.put("value", str());
        command.setUriParams(pathList);
        return command;
    }

}
