package com.oc.hawk.transfer.entrypoint.api.dto;

import lombok.Data;

@Data
public class UserGroupDTO {
	
	private Long groupId;
	private String groupName;
	private Integer groupStatus;
}
