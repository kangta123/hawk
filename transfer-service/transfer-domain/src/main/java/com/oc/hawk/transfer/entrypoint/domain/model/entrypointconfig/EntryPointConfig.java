package com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig;

import lombok.Builder;
import lombok.Getter;
import com.oc.hawk.ddd.DomainEntity;

@Getter
@Builder
@DomainEntity
public class EntryPointConfig{
	
	/**
	 * 配置id
	 */
	private EntryPointConfigID configId;
	/**
	 * 配置：名称、配置描述
	 */
	private EntryPointDesign design;
	/**
	 * 配置分组
	 */
	private EntryPointGroupID groupId;
	/**
	 * 配置资源
	 */
	private EntryPointHttpResource httpResource;
	
}