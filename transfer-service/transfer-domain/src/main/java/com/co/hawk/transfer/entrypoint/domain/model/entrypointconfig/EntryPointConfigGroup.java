package com.co.hawk.transfer.entrypoint.domain.model.entrypointconfig;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.oc.hawk.ddd.DomainEntity;
import lombok.Builder;
import lombok.Getter;

@DomainEntity
@Getter
@Builder
public class EntryPointConfigGroup {
	
	private EntryPointGroupID groupId;
	private String groupName;
	
	public void updateGroupName(String groupName) {
		if(StringUtils.isNotBlank(groupName)) {
			this.groupName = groupName;
		}
	}
	
	public void updateGroupId(EntryPointGroupID groupId) {
		if(Objects.nonNull(groupId)) {
			this.groupId = groupId;
		}
	}
	
}