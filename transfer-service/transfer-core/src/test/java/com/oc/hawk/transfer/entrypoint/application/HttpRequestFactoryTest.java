package com.oc.hawk.transfer.entrypoint.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.oc.hawk.transfer.entrypoint.api.command.ExecuteCommand;
import com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointConfig;
import com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointConfigID;
import com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointDesign;
import com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointGroupID;
import com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointHttpResource;
import com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointMethod;
import com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointPath;
import com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointTarget;
import com.oc.hawk.transfer.entrypoint.domain.model.request.HttpRequest;

public class HttpRequestFactoryTest extends EntryPointBaseTest{
	
	HttpRequestFactory httpRequestFactory;
	
	@BeforeEach
    public void setup() {
		httpRequestFactory = new HttpRequestFactory();
    }
	
	@Test
	public void create_httpRequest() {
		EntryPointConfig entryPointConfig = getEntryPointConfig();
		ExecuteCommand executeCommand = getExecuteCommand();
		
		HttpRequest httpRequest = httpRequestFactory.create(entryPointConfig,executeCommand);
		Assertions.assertThat(httpRequest).isNotNull();
	}
	
	private EntryPointConfig getEntryPointConfig() {
		return EntryPointConfig.builder()
				.configId(new EntryPointConfigID(along()))
				.design(new EntryPointDesign(str(), str()))
				.groupId(new EntryPointGroupID(along()))
				.httpResource(new EntryPointHttpResource(new EntryPointPath(str()), EntryPointMethod.valueOf("POST"), new EntryPointTarget(str(), along())))
				.build();
	}
	
	private ExecuteCommand getExecuteCommand() {
		ExecuteCommand command = new ExecuteCommand();
		command.setBody(str());
		command.setEntryPointId(along());
		command.setInstanceName(str());
		
		List<Map<String,String>> formList = new ArrayList<>();
		Map<String,String> formMap = new HashMap<String,String>();
		formMap.put("key", str());
		formMap.put("value", str());
		formList.add(formMap);
		command.setForm(formList);
		
		List<Map<String,String>> headerList = new ArrayList<>();
		Map<String,String> headerMap = new HashMap<String,String>();
		headerMap.put("key", str());
		headerMap.put("value", str());
		command.setHeader(headerList);
		
		List<Map<String,String>> pathList = new ArrayList<>();
		Map<String,String> pathMap = new HashMap<String,String>();
		pathMap.put("key", str());
		pathMap.put("value", str());
		command.setPath(pathList);
		return command;
	}
	
}
