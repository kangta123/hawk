package com.oc.hawk.traffic.entrypoint.api.dto;

import lombok.Data;

@Data
public class UserGroupDTO {

    private Long groupId;
	private String groupName;
	private Integer groupStatus;
}
