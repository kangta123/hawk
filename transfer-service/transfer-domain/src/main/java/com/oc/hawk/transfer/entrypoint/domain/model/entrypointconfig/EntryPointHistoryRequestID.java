package com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig;

import com.oc.hawk.ddd.DomainValueObject;
import lombok.Getter;
import lombok.NoArgsConstructor;

@DomainValueObject
@Getter
@NoArgsConstructor
public class EntryPointHistoryRequestID {
	
	private String requestId;
	
	public EntryPointHistoryRequestID(String requestId) {
		this.requestId = requestId;
	}
	
}
