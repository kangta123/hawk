package com.oc.hawk.traffic.entrypoint.domain.service;

import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.*;
import com.oc.hawk.traffic.entrypoint.domain.model.httpresource.HttpPath;
import com.oc.hawk.traffic.entrypoint.domain.model.httpresource.HttpResource;
import com.oc.hawk.traffic.entrypoint.domain.model.trace.TraceId;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class EntryPointConfigGroups {

    private final EntryPointConfigRepository apiConfigRepository;

    /**
     * 查询当期可见接口
     */
    public List<EntryPointConfig> getCurrentEntryPointVisibilitiy(List<EntryPointConfigGroup> entryPointGroupList) {
        //查询可见接口
        List<EntryPointGroupID> groupIdList = entryPointGroupList.stream().map(obj -> obj.getGroupId()).collect(Collectors.toList());
        return apiConfigRepository.byGroupIdList(groupIdList);
    }

    /**
     * 查询可见分组列表
     */
    public List<EntryPointConfigGroup> getCurrentGroupList() {
        //查询可见分组
        return apiConfigRepository.findGroups();
    }

    /**
     * 查询所有分组
     */
    public List<EntryPointConfigGroup> getCurrentAllGroupList() {
        return apiConfigRepository.findAllGroup();
    }

    /**
     * 更新分组可见度
     */
    public void updateVisibility(List<EntryPointGroupID> entryPointGroupIdList, boolean isVisibility) {
        if (!isVisibility) {
            apiConfigRepository.update(entryPointGroupIdList);
        }
    }

    /**
     * 根据关键字模糊查询接口配置
     */
    public List<EntryPointConfig> getEntryPointByKey(String key) {
        //用户可见组
        List<EntryPointConfigGroup> groupList = getCurrentGroupList();
        EntryPointConfig config = EntryPointConfig.builder().description(new EntryPointDescription(key, key))
            .httpResource(new HttpResource(new HttpPath(key), null))
            .build();
        List<EntryPointConfig> keyList = apiConfigRepository.byKey(config, groupList);
		return keyList;
	}
    
    /**
     * 根据id删除api配置
     */
    public void deleteEntryPoint(EntryPointConfigID entryPointConfigId) {
        apiConfigRepository.deleteById(entryPointConfigId);
    }
    
    /**
     * 查询api对应全部分组列表
     */
    public List<EntryPointConfigGroup> getUserGroupList(List<EntryPointConfig> entryPointConfigList) {
        List<EntryPointGroupID> idList = entryPointConfigList.stream().map(obj -> obj.getGroupId()).collect(Collectors.toList());
        return apiConfigRepository.byIdList(idList);
    }
    
    /**
     * 查询全部api配置
     */
    public List<EntryPointConfig> getEntryPointConfigList(){
        return apiConfigRepository.findAllEntryPointConfig();
    }
    
    /**
     * 删除分组
     */
    public void deleteEntryPointGroup(EntryPointGroupID groupId) {
        apiConfigRepository.deleteGroupById(groupId);
    }
    
}
