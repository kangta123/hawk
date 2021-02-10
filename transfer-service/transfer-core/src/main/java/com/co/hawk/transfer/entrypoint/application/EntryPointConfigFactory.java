package com.co.hawk.transfer.entrypoint.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import com.co.hawk.transfer.entrypoint.api.command.CreateEntryPointCommand;
import com.co.hawk.transfer.entrypoint.api.dto.ImportGroupDTO.ImportApiDTO;
import com.co.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointConfig;
import com.co.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointDesign;
import com.co.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointGroupID;
import com.co.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointGroupVisibility;
import com.co.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointHttpResource;
import com.co.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointMethodType;
import com.co.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointPath;
import com.co.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointTarget;
import com.oc.hawk.api.constant.AccountHolder;
import com.oc.hawk.common.utils.AccountHolderUtils;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EntryPointConfigFactory {

	public EntryPointConfig create(CreateEntryPointCommand command) {
		EntryPointGroupID groupId = new EntryPointGroupID(command.getGroupId());
		
		EntryPointDesign design = new EntryPointDesign(command.getName(), command.getDesc());
		
		EntryPointPath apiPath = new EntryPointPath(command.getPath());
		EntryPointMethodType apiMethod = EntryPointMethodType.valueOf(command.getMethod());
		EntryPointTarget apiTarget = new EntryPointTarget(command.getApp(),command.getProjectId());
		
		EntryPointHttpResource apiHttpResource = new EntryPointHttpResource(apiPath,apiMethod,apiTarget);
		
		return EntryPointConfig.builder()
				.groupId(groupId)
				.design(design)
				.httpResource(apiHttpResource)
				.build();
	}
	
	public List<EntryPointConfig> create(List<ImportApiDTO> importApiList){
		List<EntryPointConfig> baseApiConfigList = new ArrayList<EntryPointConfig>();
		for(ImportApiDTO importApiDTO : importApiList) {
			EntryPointConfig apiConfig = EntryPointConfig.builder()
					.design(new EntryPointDesign(importApiDTO.getName(),importApiDTO.getDescription()))
					.httpResource(new EntryPointHttpResource(new EntryPointPath(handleImportApiUrl(importApiDTO.getUrl())),EntryPointMethodType.valueOf(importApiDTO.getMethod()) , new EntryPointTarget()))
					.build();
			baseApiConfigList.add(apiConfig);
		}
		return baseApiConfigList;
	}
	
	private String handleImportApiUrl(String url) {
		String replaceStr = url.replaceAll("http://", "").replaceAll("https://", "");
		int pos = replaceStr.indexOf("/");
		return replaceStr.substring(pos);
	}
	
	public EntryPointGroupVisibility createGroupVisibility(List<Long> groupIdList) {
		AccountHolder holder = AccountHolderUtils.getAccountHolder();
		List<EntryPointGroupID> groupIDList = groupIdList.stream().map(obj -> new EntryPointGroupID(obj)).collect(Collectors.toList());
		return EntryPointGroupVisibility.create(holder.getId(), groupIDList);
	}
}
