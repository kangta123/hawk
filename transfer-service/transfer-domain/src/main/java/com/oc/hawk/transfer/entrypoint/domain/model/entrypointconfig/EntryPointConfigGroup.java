package com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig;

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
	private EntryPointGroupVisibility entryPointGroupVisibility;
	
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
	
	public void updateEntryPointGroupVisibility(EntryPointGroupVisibility visiblity) {
		this.entryPointGroupVisibility = visiblity;
	}
	
	@Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof EntryPointConfigGroup)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        EntryPointConfigGroup group = (EntryPointConfigGroup) obj;
        if(group.getGroupId()==null) {
        	return false;
        }
        return group.getGroupId().getId().equals(this.groupId.getId());
    }
	
}