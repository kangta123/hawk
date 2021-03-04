package com.oc.hawk.transfer.entrypoint.port.driven.persistence.po;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;

import com.oc.hawk.common.hibernate.BaseEntity;
import com.oc.hawk.common.utils.DateUtils;
import com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointConfigGroup;
import com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointGroupID;
import com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointGroupVisibility;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "transfer_endpoint_group")
@Entity
@DynamicUpdate
public class EntryPointConfigGroupPO extends BaseEntity{
	
	private String groupName;
	@CreatedDate
	private Timestamp createTime;
	@CreatedDate
	private Timestamp updateTime;
	
	@PreUpdate
    protected void onUpdate() {
		updateTime = new Timestamp(System.currentTimeMillis());
    }
	
	public static EntryPointConfigGroupPO createBy(EntryPointConfigGroup group) {
		EntryPointConfigGroupPO groupPO = new EntryPointConfigGroupPO();
		groupPO.setGroupName(group.getGroupName());
		groupPO.setCreateTime(new Timestamp(new Date().getTime()));
		groupPO.setUpdateTime(new Timestamp(new Date().getTime()));
		return groupPO;
	}
	
	public EntryPointConfigGroup toEntryPointConfigGroup() {
		return EntryPointConfigGroup.builder()
			.groupId(new EntryPointGroupID(getId()))
			.groupName(groupName).build();
	}
	
}
