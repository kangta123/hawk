package com.oc.hawk.traffic.port.driving.facade.rest;

import com.oc.hawk.api.utils.JsonUtils;
import com.oc.hawk.common.spring.mvc.BooleanWrapper;
import com.oc.hawk.ddd.web.DomainPage;
import com.oc.hawk.traffic.application.entrypoint.EntryPointUseCase;
import com.oc.hawk.traffic.entrypoint.api.command.CreateEntryPointCommand;
import com.oc.hawk.traffic.entrypoint.api.command.ExecuteCommand;
import com.oc.hawk.traffic.entrypoint.api.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
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
    public BooleanWrapper createUserGroup(@RequestParam(required = false) String groupName) {
        entryPointUseCase.createGroup(groupName);
        return BooleanWrapper.TRUE;
    }

    /**
     * 查询用户所有可见分组及分组下的api
     */
    @GetMapping("")
    public List<UserGroupEntryPointDTO> queryUserGroupApiList() {
        return entryPointUseCase.queryGroupAndApiList();
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
     * 根据api地址模糊查询
     */
    @GetMapping("/path")
    public List<UserEntryPointDTO> queryApiByPath(@RequestParam(required = false) String key) {
        return entryPointUseCase.queryApiByResource(key);
    }

    /**
     * 根据id查询api详情信息
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
    @PostMapping("/execution")
    public ExecuteResponseDTO executor(@RequestBody ExecuteCommand executeCommand) {
        return entryPointUseCase.execute(executeCommand);
    }

    /**
     * 根据接口id,查询历史请求列表
     */
    @GetMapping("/history/page")
    public DomainPage<EntryPointHistoryListItemDTO> queryApiHistoryList(@PathVariable Long id) {

        return null;
    }

    /**
     * 根据历史请求id,查询单个历史请求信息
     */
    @GetMapping("/history/{id}")
    public EntryPointHistoryDetailDTO queryApiHistoryInfo(@PathVariable Long id) {

        return null;
    }

    /**
     * 删除接口
     */
    @DeleteMapping("/{id}")
    public BooleanWrapper deleteEntryPoint(@PathVariable Long id) {

        return null;
    }
}
