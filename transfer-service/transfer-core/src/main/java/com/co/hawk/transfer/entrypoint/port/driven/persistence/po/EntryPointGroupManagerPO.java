package com.co.hawk.transfer.entrypoint.port.driven.persistence.po;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.Entity;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;

import com.co.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointGroupID;
import com.co.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointGroupVisibility;
import com.google.common.base.Joiner;
import com.oc.hawk.common.hibernate.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "transfer_endpoint_group_manager")
@Entity
@DynamicUpdate
public class EntryPointGroupManagerPO extends BaseEntity{
	
	private Long userId;
	private String groupids;
	
	@CreatedDate
	private Timestamp createTime;
	@CreatedDate
	private Timestamp updateTime;
	
	@PreUpdate
    protected void onUpdate() {
        updateTime = new Timestamp(System.currentTimeMillis());
    }
	
	public static EntryPointGroupManagerPO createBy(EntryPointGroupVisibility apiGroupManager) {
		EntryPointGroupManagerPO po = new EntryPointGroupManagerPO();
		po.setUserId(apiGroupManager.getUserId());
		
		List<EntryPointGroupID> entryPointGroupIDList = apiGroupManager.getGroupids();
		String groupids = Joiner.on(",").join(entryPointGroupIDList);
		po.setGroupids(groupids);
		return po;
	}
	
	public EntryPointGroupVisibility toEntryPointGroupManager() {
		List<EntryPointGroupID> groupIDList= Stream.of(groupids.split(",")).map(obj -> new EntryPointGroupID(Long.parseLong(obj))).collect(Collectors.toList());
		
		EntryPointGroupVisibility manager = EntryPointGroupVisibility.create(this.userId, groupIDList);
		return manager;
	}
	
}
