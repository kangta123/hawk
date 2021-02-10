package com.co.hawk.transfer.entrypoint.api.command;

import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class ExecuteCommand {
	//form表单参数
	private List<Map<String,String>> form;
	//header头
	private List<Map<String,String>> header;
	//请求体
	private String body;
	//请求路径参数
	private List<Map<String,String>> path;
	//配置接口id
	private Long configId;
	//实例名
	private String instanceName;
}
