package com.oc.hawk.traffic.port.driving.facade.rest;

import com.oc.hawk.api.utils.JsonUtils;
import com.oc.hawk.common.spring.mvc.BooleanWrapper;
import com.oc.hawk.traffic.application.entrypoint.EntryPointUseCase;
import com.oc.hawk.traffic.entrypoint.api.command.CreateEntryPointCommand;
import com.oc.hawk.traffic.entrypoint.api.command.CreateGroupCommand;
import com.oc.hawk.traffic.entrypoint.api.command.ExecuteCommand;
import com.oc.hawk.traffic.entrypoint.api.dto.*;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointConfigID;
import com.oc.hawk.traffic.entrypoint.domain.model.trace.TraceId;
import io.micrometer.core.instrument.util.IOUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/entrypoint")
@RequiredArgsConstructor
@Slf4j
public class EntryPointController {

    private final EntryPointUseCase entryPointUseCase;

    /**
     * 创建API
     */
    @PostMapping("")
    public BooleanWrapper createApi(@RequestBody CreateEntryPointCommand apiCommand) {
        entryPointUseCase.createApi(apiCommand);
        return BooleanWrapper.TRUE;
    }

    /**
     * 创建分组
     */
    @PostMapping("/group")
    public BooleanWrapper createUserGroup(@RequestBody CreateGroupCommand groupCommand) {
        entryPointUseCase.createGroup(groupCommand.getGroupName());
        return BooleanWrapper.TRUE;
    }

    /**
     * 查询用户所有可见分组及分组下的api
     */
    @GetMapping("")
    public List<UserGroupEntryPointDTO> queryUserGroupApiList(@RequestParam(required = false) String key) {
        if(StringUtils.isEmpty(key)) {
            return entryPointUseCase.queryGroupAndApiList();
        }else{
            return entryPointUseCase.queryApiByResource(key);
        }
    }

    /**
     * 查询用户所有分组及用户可见分组
     */
    @GetMapping("/group")
    public List<UserGroupDTO> queryUserAllGroupList() {
        return entryPointUseCase.queryUserAllGroupList();
    }

    /**
     * 设置分组可见度
     */
    @PutMapping("/group/visibility")
    public BooleanWrapper updateUserGroupVisibility(@RequestBody List<Long> groupIds) {
        entryPointUseCase.updateUserGroupVisibility(groupIds);
        return BooleanWrapper.TRUE;
    }

    /**
     * 根据id查询api详情信息
     */
    @GetMapping("/{id}")
    public EntryPointDTO queryApiInfo(@PathVariable Long id) {
        return entryPointUseCase.queryApiInfo(id);
    }

    /**
     * postman内容导入
     */
    @PostMapping("/group/{id}/importance")
    public BooleanWrapper importGroup(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws Exception{
        String importJson = IOUtils.toString(file.getInputStream());
        ImportGroupDTO importGroupDTO = JsonUtils.json2Object(importJson, ImportGroupDTO.class);
        entryPointUseCase.importGroup(id,importGroupDTO);
        return BooleanWrapper.TRUE;
    }

    /**
     * 执行接口
     */
    @PostMapping("/execution")
    public ExecuteResponseDTO executor(@RequestBody ExecuteCommand executeCommand) {
        return entryPointUseCase.execute(executeCommand);
    }

    /**
     * 根据接口id,查询历史请求列表
     */
    @GetMapping("/{entryPointId}/trace/page")
    public TraceResponseDTO queryTrafficTraceList(@RequestParam(required=false) Integer page,
            @RequestParam(required=false) Integer size,
            @PathVariable Long entryPointId) {
        return entryPointUseCase.queryTrafficTraceList(page,size,entryPointId);
    }

    /**
     * 根据历史请求id,查询单个历史请求信息
     */
    @GetMapping("/{entryPointId}/trace/{id}")
    public TraceDetailDTO queryTrafficTraceInfo(@PathVariable Long entryPointId,@PathVariable Long id) {
        return entryPointUseCase.queryTrafficTraceInfo(new EntryPointConfigID(entryPointId),new TraceId(id));
    }
    
    /**
     * 删除接口
     */
    @DeleteMapping("/{id}")
    public BooleanWrapper deleteEntryPoint(@PathVariable Long id) {
        entryPointUseCase.deleteEntryPoint(id);
        return BooleanWrapper.TRUE;
    }
    
    /**
     * 删除分组
     */
    @DeleteMapping("/group/{id}")
    public BooleanWrapper deleteEntryPointGroup(@PathVariable Long id) {
        entryPointUseCase.deleteEntryPointGroup(id);
        return BooleanWrapper.TRUE;
    }
    
}
