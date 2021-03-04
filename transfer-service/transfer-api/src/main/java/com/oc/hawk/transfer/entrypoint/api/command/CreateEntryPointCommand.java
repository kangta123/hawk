package com.oc.hawk.transfer.entrypoint.api.command;

import lombok.Data;

@Data
public class CreateEntryPointCommand {
	private Long groupId;
	private String name;
	private String path;
	private String method;
	private String desc;
	private Long projectId;
	private String app;
}
