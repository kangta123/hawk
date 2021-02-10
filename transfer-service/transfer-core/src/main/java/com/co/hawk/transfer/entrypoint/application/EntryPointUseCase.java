package com.co.hawk.transfer.entrypoint.application;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.co.hawk.transfer.entrypoint.api.command.CreateEntryPointCommand;
import com.co.hawk.transfer.entrypoint.api.command.ExecuteCommand;
import com.co.hawk.transfer.entrypoint.api.dto.ExecuteResponseDTO;
import com.co.hawk.transfer.entrypoint.api.dto.ImportGroupDTO;
import com.co.hawk.transfer.entrypoint.api.dto.UserEntryPointDTO;
import com.co.hawk.transfer.entrypoint.api.dto.UserGroupEntryPointDTO;
import com.co.hawk.transfer.entrypoint.api.dto.UserGroupDTO;
import com.co.hawk.transfer.entrypoint.application.representation.EntryPointConfigRepresentation;
import com.co.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointConfig;
import com.co.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointConfigGroup;
import com.co.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointConfigID;
import com.co.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointConfigRepository;
import com.co.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointGroupVisibility;
import com.co.hawk.transfer.entrypoint.domain.model.request.HttpRequest;
import com.co.hawk.transfer.entrypoint.domain.model.response.HttpResponse;
import com.co.hawk.transfer.entrypoint.domain.service.EntryPointConfigGroups;
import com.co.hawk.transfer.entrypoint.domain.service.EntryPointConfigExecutor;
import com.co.hawk.transfer.entrypoint.domain.service.EntryPointGroupImportance;
import com.co.hawk.transfer.entrypoint.port.driven.facade.excutor.RestTemplateRequestExecutor;

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
	private final RestTemplateRequestExecutor restTemplateRequestExecutor;
	/**
	 * 查询可见分组及接口
	 */
	public List<UserGroupEntryPointDTO> queryGroupAndApiList(){
		Map<EntryPointConfigGroup,List<EntryPointConfig>> entryPointVisibilyMap 
			= new EntryPointConfigGroups(entryPointConfigRepository).getCurrentEntryPointVisibily();
		return entryPointConfigRepresentation.toUserGroupEntryPointDTOList(entryPointVisibilyMap);
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
	 * 查询全部接口列表
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
		EntryPointGroupVisibility groupVisibility = entryPointConfigFactory.createGroupVisibility(groupIds);
		new EntryPointConfigGroups(entryPointConfigRepository).updateUserGroupVisibility(groupVisibility);
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
		UserEntryPointDTO userApiDTO = entryPointConfigRepresentation.toUserEntryPointDTO(config, config.getGroupId().getId());
		return userApiDTO;
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
		EntryPointConfig entryPointConfig = entryPointConfigRepository.byId(new EntryPointConfigID(executeCommand.getConfigId()));
		HttpRequest httpRequest = httpRequestFactory.create(entryPointConfig,executeCommand);
		
		HttpResponse httpResponse = new EntryPointConfigExecutor(restTemplateRequestExecutor).executor(httpRequest);
		
		ExecuteResponseDTO excuteResponseDTO = entryPointConfigRepresentation.toExecuteResponseDTO(httpResponse);
		return excuteResponseDTO;
	}
	
}
