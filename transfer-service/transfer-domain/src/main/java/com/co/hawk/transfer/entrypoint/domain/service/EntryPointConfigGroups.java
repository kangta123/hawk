package com.co.hawk.transfer.entrypoint.domain.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.co.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointConfig;
import com.co.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointConfigGroup;
import com.co.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointConfigRepository;
import com.co.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointDesign;
import com.co.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointGroupVisibility;
import com.co.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointHttpResource;
import com.co.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointPath;
import com.oc.hawk.api.constant.AccountHolder;
import com.oc.hawk.common.utils.AccountHolderUtils;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EntryPointConfigGroups {
	
	private final EntryPointConfigRepository apiConfigRepository;
	
	public List<Long> getGroupIdListByIds(String groupids){
		List<String> list = Stream.of(groupids.split(",")).collect(Collectors.toList());
		List<Long> groupIdList = list.stream().map(obj -> Long.parseLong(obj)).collect(Collectors.toList());
		return groupIdList;
	}
	
	/**
	 * 查询当期可见接口
	 */
	public Map<EntryPointConfigGroup,List<EntryPointConfig>> getCurrentEntryPointVisibily() {
		//查询可见
		List<EntryPointConfigGroup> groupList = getCurrentGroupList();
		
		Map<EntryPointConfigGroup,List<EntryPointConfig>> entryPointVisibilyMap = new HashMap<>();
		for(EntryPointConfigGroup group : groupList) {
			List<EntryPointConfig> entryPointList = apiConfigRepository.byGroupId(group.getGroupId().getId());
			entryPointVisibilyMap.put(group, entryPointList);
		}
		return entryPointVisibilyMap;
	}
	
	/**
	 * 查询可见分组列表
	 */
	public List<EntryPointConfigGroup> getCurrentGroupList() {
		//查询可见分组
		AccountHolder holder = AccountHolderUtils.getAccountHolder();
		List<EntryPointConfigGroup> allGroupList = apiConfigRepository.findAllGroup();
		List<EntryPointConfigGroup> invisibleGroupList = apiConfigRepository.findGroupsByUserId(holder.getId());
		//可见分组
		allGroupList.removeAll(invisibleGroupList);
		return allGroupList;
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
	public void updateUserGroupVisibility(EntryPointGroupVisibility groupVisibility) {
		AccountHolder holder = AccountHolderUtils.getAccountHolder();
		apiConfigRepository.update(holder.getId(), groupVisibility);
	}
	
	/**
	 * 根据关键字模糊查询接口配置
	 */
	public List<EntryPointConfig> getEntryPointByKey(String key) {
		//用户可见组
		List<EntryPointConfigGroup> groupList = getCurrentGroupList();
		List<Long> groupIdList = groupList.stream().map(obj -> obj.getGroupId().getId()).collect(Collectors.toList());
		EntryPointConfig config = EntryPointConfig.builder().design(new EntryPointDesign(key,key))
															.httpResource(new EntryPointHttpResource(new EntryPointPath(key), null, null))
															.build();
		List<EntryPointConfig> keyList = apiConfigRepository.byKey(config);
		List<EntryPointConfig> resultList = keyList.stream().filter(obj -> groupIdList.contains(obj.getGroupId().getId())).collect(Collectors.toList());
		return resultList;
	}
}
