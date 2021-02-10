package com.co.hawk.transfer.entrypoint.port.driving.facade.rest;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.co.hawk.transfer.entrypoint.api.command.CreateEntryPointCommand;
import com.co.hawk.transfer.entrypoint.api.command.ExecuteCommand;
import com.co.hawk.transfer.entrypoint.api.dto.ExecuteResponseDTO;
import com.co.hawk.transfer.entrypoint.api.dto.ImportGroupDTO;
import com.co.hawk.transfer.entrypoint.api.dto.UserEntryPointDTO;
import com.co.hawk.transfer.entrypoint.api.dto.UserGroupEntryPointDTO;
import com.co.hawk.transfer.entrypoint.api.dto.UserGroupDTO;
import com.co.hawk.transfer.entrypoint.application.EntryPointUseCase;
import com.oc.hawk.api.utils.JsonUtils;
import com.oc.hawk.common.spring.mvc.BooleanWrapper;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EntryPointController {
	
	private final EntryPointUseCase entryPointUseCase;
	
	/**
	 * 创建API
	 * 
	 */
	@PostMapping("")
	public BooleanWrapper createApi(@RequestBody CreateEntryPointCommand apiCommand) {
		entryPointUseCase.createApi(apiCommand);
		return BooleanWrapper.TRUE;
	}
	
	/**
	 * 创建分组
	 * 
	 */
	@PostMapping("/group")
	public BooleanWrapper createUserGroup(@RequestParam(required=false) String groupName) {
		entryPointUseCase.createGroup(groupName);
		return BooleanWrapper.TRUE;
	}
	
	/**
	 * 查询用户所有可见分组及分组下的api
	 * 
	 */
	@GetMapping("")
	public List<UserGroupEntryPointDTO> queryUserGroupApiList() {
		return entryPointUseCase.queryGroupAndApiList();
	}
	
	/**
	 * 查询用户所有分组及用户可见分组
	 * 
	 */
	@GetMapping("/group")
	public List<UserGroupDTO> queryUserAllGroupList() {
		return entryPointUseCase.queryUserAllGroupList();
	}
	
	/**
	 * 设置分组可见度
	 * 
	 */
	@PutMapping("/group/visibility")
	public BooleanWrapper updateUserGroupVisibility(@RequestParam(required=false) List<Long> groupIds) {
		entryPointUseCase.updateUserGroupVisibility(groupIds);
		return BooleanWrapper.TRUE;
	}
	
	/**
	 * 根据api地址模糊查询
	 * 
	 */
	@GetMapping("/path")
	public List<UserEntryPointDTO> queryApiByPath(@RequestParam(required=false) String key) {
		return entryPointUseCase.queryApiByResource(key);
	}
	
	/**
	 * 根据id查询api详情信息
	 * 
	 */
	@GetMapping("/info/{id}")
	public UserEntryPointDTO queryApiInfo(@PathVariable Long id) {
		return entryPointUseCase.queryApiInfo(id);
	}
	
	/**
	 * postman内容导入
	 */
	@PostMapping("/group/importance")
	public BooleanWrapper importGroup(@RequestBody String importJson) {
		ImportGroupDTO importGroupDTO = JsonUtils.json2Object(importJson, ImportGroupDTO.class);
		entryPointUseCase.importGroup(importGroupDTO);
		return BooleanWrapper.TRUE;
	}
	
	/**
	 * 执行接口
	 */
	@PostMapping("/executor")
	public ExecuteResponseDTO executor(@RequestBody ExecuteCommand executeCommand) {
		return entryPointUseCase.execute(executeCommand);
	}
	
}
