package com.co.hawk.transfer.entrypoint.domain.model.entrypointconfig;

import com.oc.hawk.ddd.DomainValueObject;
import lombok.Getter;
import lombok.NoArgsConstructor;

@DomainValueObject
@Getter
@NoArgsConstructor
public class EntryPointTarget {
	
	private String app;
	private Long projectId;
	
	public EntryPointTarget(String app,Long projectId) {
		this.app = app;
		this.projectId = projectId;
	}
	
}
