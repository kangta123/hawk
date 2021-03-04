package com.oc.hawk.transfer.entrypoint.api.dto;

import java.util.List;
import lombok.Data;

@Data
public class UserGroupEntryPointDTO {
	
	private Long groupId;
	private String groupName;
	private List<UserEntryPointDTO> apiList;
	
}
