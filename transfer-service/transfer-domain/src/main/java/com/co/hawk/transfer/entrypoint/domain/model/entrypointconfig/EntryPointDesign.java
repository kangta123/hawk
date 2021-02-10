package com.co.hawk.transfer.entrypoint.domain.model.entrypointconfig;

import com.oc.hawk.ddd.DomainValueObject;

import lombok.Getter;
import lombok.NoArgsConstructor;

@DomainValueObject
@Getter
@NoArgsConstructor
public class EntryPointDesign {
	
	private String name;
	private String desc;
	
	public EntryPointDesign(String name,String desc) {
		this.desc = name;
		this.desc = desc;
	}
}
