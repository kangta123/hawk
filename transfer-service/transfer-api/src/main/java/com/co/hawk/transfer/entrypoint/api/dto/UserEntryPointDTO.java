package com.co.hawk.transfer.entrypoint.api.dto;

import lombok.Data;

@Data
public class UserEntryPointDTO {
	
	private Long id;
	private Long groupId;
	private String apiName;
	private String apiPath;
	private String apiMethod;
	private String apiDesc;
	private String projectId;
	private String app;
	
}
