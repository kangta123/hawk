package com.co.hawk.transfer.entrypoint.domain.model.entrypointconfig;

import java.util.List;

public interface EntryPointConfigRepository {
	
	EntryPointConfigID save(EntryPointConfig config);
	
	EntryPointConfig byId(EntryPointConfigID id);
	
	List<EntryPointConfig> byGroupId(Long id);
	
	EntryPointGroupID save(EntryPointConfigGroup group);
	
	List<EntryPointConfig> byKey(EntryPointConfig config);
	
	List<EntryPointConfigGroup> findAllGroup();
	
	EntryPointConfigGroup byId(EntryPointGroupID id);
	
	List<EntryPointConfigGroup> findGroupsByUserId(Long userId);
	
	void update(Long userId,EntryPointGroupVisibility visibility);
	
	List<EntryPointConfigGroup> byIdList(List<Long> groupIdList);
	
	List<EntryPointConfig> byGroupIdList(List<Long> groupIdList);
	
	//EntryPointGroupVisibility byGroupManagerUserId(Long userId);
	
	void updateList(Long userId,List<EntryPointConfigGroup> groupList);
	
	void batchSave(EntryPointGroupID entryPointConfigGroupID,List<EntryPointConfig> apiConfigList);
}
