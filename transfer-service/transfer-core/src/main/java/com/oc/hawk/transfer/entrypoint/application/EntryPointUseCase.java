package com.oc.hawk.transfer.entrypoint.application;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.oc.hawk.transfer.entrypoint.api.command.CreateEntryPointCommand;
import com.oc.hawk.transfer.entrypoint.api.command.ExecuteCommand;
import com.oc.hawk.transfer.entrypoint.api.dto.ExecuteResponseDTO;
import com.oc.hawk.transfer.entrypoint.api.dto.ImportGroupDTO;
import com.oc.hawk.transfer.entrypoint.api.dto.UserEntryPointDTO;
import com.oc.hawk.transfer.entrypoint.api.dto.UserGroupDTO;
import com.oc.hawk.transfer.entrypoint.api.dto.UserGroupEntryPointDTO;
import com.oc.hawk.transfer.entrypoint.application.representation.EntryPointConfigRepresentation;
import com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointConfig;
import com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointConfigGroup;
import com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointConfigID;
import com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointConfigRepository;
import com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointGroupID;
import com.oc.hawk.transfer.entrypoint.domain.model.request.HttpRequest;
import com.oc.hawk.transfer.entrypoint.domain.model.response.HttpResponse;
import com.oc.hawk.transfer.entrypoint.domain.service.EntryPointConfigExecutor;
import com.oc.hawk.transfer.entrypoint.domain.service.EntryPointConfigGroups;
import com.oc.hawk.transfer.entrypoint.domain.service.EntryPointGroupImportance;
import com.oc.hawk.transfer.entrypoint.domain.service.excutor.EntryPointExcutor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class EntryPointUseCase {
	
	private final EntryPointConfigFactory entryPointConfigFactory;
	private final EntryPointConfigGroupFactory apiConfigGroupFactory;
	
	private final EntryPointConfigRepository entryPointConfigRepository;
	private final EntryPointConfigRepresentation entryPointConfigRepresentation;
	
	private final HttpRequestFactory httpRequestFactory;
	private final EntryPointExcutor entryPointExcutor;
	/**
	 * 查询可见分组及接口
	 */
	public List<UserGroupEntryPointDTO> queryGroupAndApiList(){
		List<EntryPointConfigGroup> entryPointGroupList
			= new EntryPointConfigGroups(entryPointConfigRepository).getCurrentGroupList();
		List<EntryPointConfig> entryPointVisibilyList 
			= new EntryPointConfigGroups(entryPointConfigRepository).getCurrentEntryPointVisibily(entryPointGroupList);
		return entryPointConfigRepresentation.toUserGroupEntryPointDTOList(entryPointVisibilyList,entryPointGroupList);
	}
	
	/**
	 * 创建分组
	 */
	@Transactional(rollbackFor = Exception.class)
	public void createGroup(String groupName) {
		EntryPointConfigGroup group = apiConfigGroupFactory.create(groupName);
		entryPointConfigRepository.save(group);
	}
	
	/**
	 * 创建接口配置
	 */
	@Transactional(rollbackFor = Exception.class)
	public void createApi(CreateEntryPointCommand command) {
		log.info("create api with name:{},path:{},method:{}", command.getName(),command.getPath(),command.getMethod());
		EntryPointConfig apiConfig = entryPointConfigFactory.create(command);
		entryPointConfigRepository.save(apiConfig);
	}
	
	/**
	 * 查询全部分组列表
	 */
	public List<UserGroupDTO> queryUserAllGroupList(){
		List<EntryPointConfigGroup> allGroupList = new EntryPointConfigGroups(entryPointConfigRepository).getCurrentAllGroupList();
		List<EntryPointConfigGroup> groupList = new EntryPointConfigGroups(entryPointConfigRepository).getCurrentGroupList();
		return entryPointConfigRepresentation.toUserGroupDTO(allGroupList,groupList);
	}
	
	/**
	 * 更新分组可见度
	 */
	@Transactional(rollbackFor = Exception.class)
	public void updateUserGroupVisibility(List<Long> groupIds) {
		List<EntryPointGroupID> entryPointGroupIdList = entryPointConfigFactory.createGroupInvisibility(groupIds);
		new EntryPointConfigGroups(entryPointConfigRepository).updateVisibility(entryPointGroupIdList,false);
	}
	
	/**
	 * 根据关键字模块查询接口列表
	 */
	public List<UserEntryPointDTO> queryApiByResource(String key) {
		List<EntryPointConfig> entryPointConfigList = new EntryPointConfigGroups(entryPointConfigRepository).getEntryPointByKey(key);
		List<UserEntryPointDTO> userApiDTOList = entryPointConfigRepresentation.toUserApiDTOList(entryPointConfigList);
		return userApiDTOList;
	}
	
	/**
	 * 单个接口信息查询
	 */
	public UserEntryPointDTO queryApiInfo(Long id) {
		EntryPointConfig config = entryPointConfigRepository.byId(new EntryPointConfigID(id));
		if(Objects.isNull(config)) {
			return new UserEntryPointDTO();
		}
		return entryPointConfigRepresentation.toUserEntryPointDTO(config, config.getGroupId().getId());
	}
	
	/**
	 * 导入接口
	 */
	@Transactional(rollbackFor = Exception.class)
	public void importGroup(ImportGroupDTO importGroupDTO) {
		List<EntryPointConfig> entryPointList = entryPointConfigFactory.create(importGroupDTO.getRequests());
		EntryPointConfigGroup entryPointGroup = apiConfigGroupFactory.create(importGroupDTO.getName());
		new EntryPointGroupImportance(entryPointConfigRepository).importPostmanJson(entryPointGroup,entryPointList);
	}
	
	/**
	 * 执行接口
	 */
	public ExecuteResponseDTO execute(ExecuteCommand executeCommand) {
		EntryPointConfig entryPointConfig = entryPointConfigRepository.byId(new EntryPointConfigID(executeCommand.getEntryPointId()));
		HttpRequest httpRequest = httpRequestFactory.create(entryPointConfig,executeCommand);
		
		HttpResponse httpResponse = new EntryPointConfigExecutor(entryPointExcutor).execute(httpRequest);
		
		ExecuteResponseDTO excuteResponseDTO = entryPointConfigRepresentation.toExecuteResponseDTO(httpResponse);
		return excuteResponseDTO;
	}
	
}
