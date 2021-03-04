package com.oc.hawk.transfer.entrypoint.domain.service;

import java.util.List;
import java.util.stream.Collectors;

import com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointConfig;
import com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointConfigGroup;
import com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointConfigRepository;
import com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointDesign;
import com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointGroupID;
import com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointHttpResource;
import com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointPath;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EntryPointConfigGroups {
	
	private final EntryPointConfigRepository apiConfigRepository;
	
	/**
	 * 查询当期可见接口
	 */
	public List<EntryPointConfig> getCurrentEntryPointVisibily(List<EntryPointConfigGroup> entryPointGroupList) {
		//查询可见接口
		List<Long> groupIdList = entryPointGroupList.stream().map(obj -> obj.getGroupId().getId()).collect(Collectors.toList());
		return apiConfigRepository.byGroupIdList(groupIdList);
	}
	
	/**
	 * 查询可见分组列表
	 */
	public List<EntryPointConfigGroup> getCurrentGroupList() {
		//查询可见分组
		List<EntryPointConfigGroup> visibleGroupList = apiConfigRepository.findGroups();
		return visibleGroupList;
	}
	
	/**
	 * 查询所有分组
	 */
	public List<EntryPointConfigGroup> getCurrentAllGroupList(){
		return apiConfigRepository.findAllGroup();
	}
	
	/**
	 * 更新分组可见度
	 */
	public void updateVisibility(List<EntryPointGroupID> entryPointGroupIdList,boolean isVisibility) {
		if(!isVisibility) {
			apiConfigRepository.update(entryPointGroupIdList);
		}
	}
	
	/**
	 * 根据关键字模糊查询接口配置
	 */
	public List<EntryPointConfig> getEntryPointByKey(String key) {
		//用户可见组
		List<EntryPointConfigGroup> groupList = getCurrentGroupList();
//		List<Long> groupIdList = groupList.stream().map(obj -> obj.getGroupId().getId()).collect(Collectors.toList());
		EntryPointConfig config = EntryPointConfig.builder().design(new EntryPointDesign(key,key))
															.httpResource(new EntryPointHttpResource(new EntryPointPath(key), null, null))
															.build();
		List<EntryPointConfig> keyList = apiConfigRepository.byKey(config,groupList);
		//List<EntryPointConfig> resultList = keyList.stream().filter(obj -> groupIdList.contains(obj.getGroupId().getId())).collect(Collectors.toList());
		return keyList;
	}
}
