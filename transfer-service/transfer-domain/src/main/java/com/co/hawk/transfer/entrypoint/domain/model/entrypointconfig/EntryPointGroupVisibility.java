package com.co.hawk.transfer.entrypoint.domain.model.entrypointconfig;

import java.util.List;

import com.oc.hawk.ddd.DomainValueObject;
import lombok.Getter;
import lombok.NoArgsConstructor;

@DomainValueObject
@Getter
@NoArgsConstructor
public class EntryPointGroupVisibility {
	
	private Long userId;
	private List<EntryPointGroupID> groupids;
	
	public static EntryPointGroupVisibility create(Long userId,List<EntryPointGroupID> groupids) {
		EntryPointGroupVisibility manager = new EntryPointGroupVisibility();
		manager.userId = userId;
		manager.groupids = groupids;
		return manager;
	}
	
}
