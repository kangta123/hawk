package com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig;

import com.oc.hawk.ddd.DomainValueObject;
import lombok.Getter;
import lombok.NoArgsConstructor;

@DomainValueObject
@Getter
@NoArgsConstructor
public class EntryPointGroupVisibility {
	
	private boolean visibility = true;
	
	public static EntryPointGroupVisibility create(boolean visibility) {
		EntryPointGroupVisibility entryPointGroupVisibility = new EntryPointGroupVisibility();
		entryPointGroupVisibility.visibility = visibility;
		return entryPointGroupVisibility;
	}
}
