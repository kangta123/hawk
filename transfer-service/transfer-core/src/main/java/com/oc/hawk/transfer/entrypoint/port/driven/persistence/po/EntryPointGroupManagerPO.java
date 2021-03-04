package com.oc.hawk.transfer.entrypoint.port.driven.persistence.po;

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

import com.google.common.base.Joiner;
import com.oc.hawk.api.constant.AccountHolder;
import com.oc.hawk.common.hibernate.BaseEntity;
import com.oc.hawk.common.utils.AccountHolderUtils;
import com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointGroupID;
import com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointGroupVisibility;

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
	
	public static EntryPointGroupManagerPO createBy(Long userId,List<EntryPointGroupID> entryPointGroupIDList) {
		EntryPointGroupManagerPO po = new EntryPointGroupManagerPO();
		po.setUserId(userId);
		String groupids = Joiner.on(",").join(entryPointGroupIDList);
		po.setGroupids(groupids);
		return po;
	}
	
}
