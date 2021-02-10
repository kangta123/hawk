package com.co.hawk.transfer.entrypoint.domain.service;

import java.util.List;

import com.co.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointConfig;
import com.co.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointConfigGroup;
import com.co.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointConfigRepository;
import com.co.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointGroupID;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EntryPointGroupImportance {
	
	private final EntryPointConfigRepository apiConfigRepository;
	
	public void importPostmanJson(EntryPointConfigGroup entryPointGroup,List<EntryPointConfig> entryPointList) {
		//保存导入分组
		EntryPointGroupID apiConfigGroupID = apiConfigRepository.save(entryPointGroup);
		//保存导入api
		apiConfigRepository.batchSave(apiConfigGroupID,entryPointList);
		
		//String groupName = importGroupDTO.getName();
		//AccountHolder holder = AccountHolderUtils.getAccountHolder();
		//List<EntryPointConfigGroup> groupList = apiConfigRepository.findGroupsByUserId(holder.getId());
		//EntryPointConfigGroup newGroup = apiConfigGroupFactory.create(holder.getId(),groupName,new ArrayList<EntryPointConfigGroup>());
		
//		if(Objects.isNull(apiConfigGroupID) || Objects.isNull(apiConfigGroupID.getId())) {
//			throw new ApiGroupNotFoundException();
//		}
//		groupList.add(newGroup);
		
		//保存关联表
		//apiConfigRepository.updateList(holder.getId(), groupList);
	}
	
}
