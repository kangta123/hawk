package com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig;

import com.oc.hawk.ddd.DomainValueObject;

import lombok.Getter;
import lombok.NoArgsConstructor;

@DomainValueObject
@Getter
@NoArgsConstructor
public class EntryPointPath {
	private String path;
	
	public EntryPointPath(String path) {
		this.path=path;
	}
}
