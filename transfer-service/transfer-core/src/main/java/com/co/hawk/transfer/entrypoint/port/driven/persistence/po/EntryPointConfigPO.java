package com.co.hawk.transfer.entrypoint.port.driven.persistence.po;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;

import com.co.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointConfig;
import com.co.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointConfigID;
import com.co.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointDesign;
import com.co.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointGroupID;
import com.co.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointHttpResource;
import com.co.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointMethodType;
import com.co.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointPath;
import com.co.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointTarget;
import com.oc.hawk.common.hibernate.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "transfer_endpoint_api")
@Entity
@DynamicUpdate
public class EntryPointConfigPO extends BaseEntity{
	
	private Long groupId;
	private String apiName;
	private String apiPath;
	private String apiMethod;
	private String apiDesc;
	private Long projectId;
	private String app;
	@CreatedDate
	private Timestamp createTime;
	@CreatedDate
	private Timestamp updateTime;
	
	@PreUpdate
    protected void onUpdate() {
        updateTime = new Timestamp(System.currentTimeMillis());
    }
	
	public static EntryPointConfigPO createBy(EntryPointConfig apiConfig) {
		EntryPointConfigPO apiConfigPO = new EntryPointConfigPO();
		apiConfigPO.setApiName(apiConfig.getDesign().getName());
		apiConfigPO.setApiPath(apiConfig.getHttpResource().getPath().getPath());
		apiConfigPO.setApiMethod(apiConfig.getHttpResource().getMethod().name());
		apiConfigPO.setGroupId(apiConfig.getGroupId().getId());
		apiConfigPO.setApiDesc(apiConfig.getDesign().getDesc());
		apiConfigPO.setApp(apiConfig.getHttpResource().getTarget().getApp());
		apiConfigPO.setProjectId(apiConfig.getHttpResource().getTarget().getProjectId());
		return apiConfigPO;
	}
	
	public EntryPointConfig toEntryPointConfig() {
		return EntryPointConfig.builder()
				.configId(new EntryPointConfigID(getId()))
				.groupId(new EntryPointGroupID(groupId))
				.design(new EntryPointDesign(apiName,apiDesc))
				.httpResource(new EntryPointHttpResource(new EntryPointPath(apiPath),EntryPointMethodType.valueOf(apiDesc), new EntryPointTarget(app,projectId)))
				.build();
	}
	
}
