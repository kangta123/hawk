package com.oc.hawk.transfer.entrypoint.application;

import org.springframework.stereotype.Component;

import com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointConfigGroup;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EntryPointConfigGroupFactory {
	
	public EntryPointConfigGroup create(String groupName) {
		return EntryPointConfigGroup.builder()
				.groupName(groupName)
				.build();
	}
	
}
