package com.co.hawk.transfer.entrypoint.application.representation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

import com.co.hawk.transfer.entrypoint.api.dto.ExecuteResponseDTO;
import com.co.hawk.transfer.entrypoint.api.dto.UserEntryPointDTO;
import com.co.hawk.transfer.entrypoint.api.dto.UserGroupEntryPointDTO;
import com.co.hawk.transfer.entrypoint.api.dto.UserGroupDTO;
import com.co.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointConfig;
import com.co.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointConfigGroup;
import com.co.hawk.transfer.entrypoint.domain.model.response.HttpResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EntryPointConfigRepresentation {
	
	public UserGroupEntryPointDTO toUserGroupEntryPointDTO(EntryPointConfigGroup group,List<EntryPointConfig> apiList){
		UserGroupEntryPointDTO userGroupApiDTO = new UserGroupEntryPointDTO();
		userGroupApiDTO.setGroupId(group.getGroupId().getId());
		userGroupApiDTO.setGroupName(group.getGroupName());
		
		List<UserEntryPointDTO> userApiDTOList = new ArrayList<UserEntryPointDTO>();
		for(EntryPointConfig config : apiList) {
			UserEntryPointDTO userApiDTO = new UserEntryPointDTO();
			
			Long id = config.getConfigId().getId();
			userApiDTO.setId(id);
			
			String name = config.getDesign().getName();
			userApiDTO.setApiName(name);
			
			userApiDTOList.add(userApiDTO);
		}
		userGroupApiDTO.setApiList(userApiDTOList);
		return userGroupApiDTO;
	}
	
	public List<UserGroupDTO> toUserGroupDTO(List<EntryPointConfigGroup> allGroupList, List<EntryPointConfigGroup> groupList) {
		List<UserGroupDTO> userGroupDTOList = new ArrayList<UserGroupDTO>();
		//转换ApiConfigGroup为groupId的list
		for(EntryPointConfigGroup group : allGroupList) {
			UserGroupDTO userGroupDTO = new UserGroupDTO();
			Long groupId = group.getGroupId().getId();
			userGroupDTO.setGroupId(groupId);
			
			String groupName = group.getGroupName();
			userGroupDTO.setGroupName(groupName);
			
			List<Long> groupIdsList = groupList.stream().map(obj -> obj.getGroupId().getId()).collect(Collectors.toList());
			
			if(groupIdsList.contains(groupId)) {
				userGroupDTO.setGroupStatus(1);
			}else{
				userGroupDTO.setGroupStatus(0);
			}
			userGroupDTOList.add(userGroupDTO);
		}
		return userGroupDTOList;
	}
	
	public UserEntryPointDTO toUserEntryPointDTO(EntryPointConfig config,Long groupId) {
		UserEntryPointDTO userApiDTO = new UserEntryPointDTO();
		userApiDTO.setId(config.getConfigId().getId());
		userApiDTO.setGroupId(groupId);
		userApiDTO.setApiName(config.getDesign().getName());
		userApiDTO.setApiPath(config.getHttpResource().getPath().getPath());
		userApiDTO.setApiMethod(config.getHttpResource().getMethod().name());
		userApiDTO.setApiDesc(config.getDesign().getDesc());
		userApiDTO.setApp(config.getHttpResource().getTarget().getApp());
		userApiDTO.setProjectId(String.valueOf(config.getHttpResource().getTarget().getProjectId()));
		return userApiDTO;
	}
	
	public List<UserGroupEntryPointDTO> toUserGroupEntryPointDTOList(Map<EntryPointConfigGroup,List<EntryPointConfig>> map) {
		List<UserGroupEntryPointDTO> userGroupApiDTOList = new ArrayList<UserGroupEntryPointDTO>();
		map.forEach((key,value) -> {
			UserGroupEntryPointDTO userGroupApiDTO = new UserGroupEntryPointDTO();
			userGroupApiDTO.setGroupId(key.getGroupId().getId());
			userGroupApiDTO.setGroupName(key.getGroupName());
			
			List<UserEntryPointDTO> userApiDTOList = new ArrayList<>();
			for(EntryPointConfig config : value) {
				userApiDTOList.add(toUserEntryPointDTO(config, key.getGroupId().getId()));   
			}
			userGroupApiDTO.setApiList(userApiDTOList);
			userGroupApiDTOList.add(userGroupApiDTO);
		});
		return userGroupApiDTOList;
	}
	
	public List<UserEntryPointDTO> toUserApiDTOList(List<EntryPointConfig> entryPointConfigList){
		List<UserEntryPointDTO> userApiDTOList = entryPointConfigList.stream().map(obj -> toUserEntryPointDTO(obj, obj.getGroupId().getId())).collect(Collectors.toList());
		return userApiDTOList;
	}
	
	public ExecuteResponseDTO toExecuteResponseDTO(HttpResponse httpResponse) {
		ExecuteResponseDTO dto = new ExecuteResponseDTO();
		dto.setResponseCode(httpResponse.getResponseCode());
		dto.setResponseTime(httpResponse.getResponseTime());
		dto.setResponseBody(httpResponse.getResponseBody().getBody());
		dto.setResponseHeader(httpResponse.getResponseHeader().getResponeseHeader());
		return dto;
	}
}
