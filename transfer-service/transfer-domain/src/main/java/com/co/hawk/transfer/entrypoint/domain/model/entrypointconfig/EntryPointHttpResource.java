package com.co.hawk.transfer.entrypoint.domain.model.entrypointconfig;

import com.oc.hawk.ddd.DomainValueObject;

import lombok.Getter;
import lombok.NoArgsConstructor;

@DomainValueObject
@Getter
@NoArgsConstructor
public class EntryPointHttpResource {
	//请求路径
	private EntryPointPath path;
	//请求方法
	private EntryPointMethodType method;
	//请求目标
	private EntryPointTarget target;
	
	public EntryPointHttpResource(EntryPointPath path,EntryPointMethodType method,EntryPointTarget target) {
		this.path = path;
		this.method = method;
		this.target = target;
	}
}
