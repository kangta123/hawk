package com.oc.hawk.traffic.port.driving.facade.rest;

import com.oc.hawk.api.utils.JsonUtils;
import com.oc.hawk.common.spring.mvc.BooleanWrapper;
import com.oc.hawk.common.utils.WebUtils;
import com.oc.hawk.traffic.application.entrypoint.EntryPointUseCase;
import com.oc.hawk.traffic.entrypoint.api.command.CreateEntryPointCommand;
import com.oc.hawk.traffic.entrypoint.api.command.CreateGroupCommand;
import com.oc.hawk.traffic.entrypoint.api.command.ExecuteCommand;
import com.oc.hawk.traffic.entrypoint.api.dto.*;
import com.oc.hawk.traffic.entrypoint.domain.model.trace.TraceId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class EntryPointController {

    private final EntryPointUseCase entryPointUseCase;

    /**
     * 创建API
     */
    @PostMapping("/entrypoint")
    public BooleanWrapper createApi(@RequestBody CreateEntryPointCommand apiCommand) {
        entryPointUseCase.createApi(apiCommand);
        return BooleanWrapper.TRUE;
    }

    /**
     * 创建分组
     */
    @PostMapping("/entrypoint/group")
    public BooleanWrapper createUserGroup(@RequestBody CreateGroupCommand groupCommand) {
        entryPointUseCase.createGroup(groupCommand.getGroupName());
        return BooleanWrapper.TRUE;
    }

    /**
     * 查询用户所有可见分组及分组下的api
     */
    @GetMapping("/entrypoint")
    public List<UserGroupEntryPointDTO> queryUserGroupApiList() {
        return entryPointUseCase.queryGroupAndApiList();
    }

    /**
     * 查询用户所有分组及用户可见分组
     */
    @GetMapping("/entrypoint/group")
    public List<UserGroupDTO> queryUserAllGroupList() {
        return entryPointUseCase.queryUserAllGroupList();
    }

    /**
     * 设置分组可见度
     */
    @PutMapping("/entrypoint/group/visibility")
    public BooleanWrapper updateUserGroupVisibility(@RequestBody List<Long> groupIds) {
        entryPointUseCase.updateUserGroupVisibility(groupIds);
        return BooleanWrapper.TRUE;
    }

    /**
     * 根据api地址模糊查询
     */
    @GetMapping("/entrypoint/path")
    public List<UserEntryPointDTO> queryApiByPath(@RequestParam(required = false) String key) {
        return entryPointUseCase.queryApiByResource(key);
    }

    /**
     * 根据id查询api详情信息
     */
    @GetMapping("/entrypoint/info/{id}")
    public UserEntryPointDTO queryApiInfo(@PathVariable Long id) {
        return entryPointUseCase.queryApiInfo(id);
    }

    /**
     * postman内容导入
     */
    @PostMapping("/entrypoint/group/importance")
    public BooleanWrapper importGroup(@RequestBody String importJson) {
        ImportGroupDTO importGroupDTO = JsonUtils.json2Object(importJson, ImportGroupDTO.class);
        entryPointUseCase.importGroup(importGroupDTO);
        return BooleanWrapper.TRUE;
    }

    /**
     * 执行接口
     */
    @PostMapping("/entrypoint/execution")
    public ExecuteResponseDTO executor(@RequestBody ExecuteCommand executeCommand) {
        return entryPointUseCase.execute(executeCommand);
    }

    /**
     * 根据接口id,查询历史请求列表
     */
    @GetMapping("/entrypoint/history/page")
    public TraceResponseDTO queryApiHistoryList(@RequestParam(required=false) Integer page,
            @RequestParam(required=false) Integer size,
            @RequestParam(required=false) Long entryPointId) {
        return entryPointUseCase.queryApiHistoryList(page,size,entryPointId);
    }

    /**
     * 根据历史请求id,查询单个历史请求信息
     */
    @GetMapping("/entrypoint/history/{id}")
    public TraceDetailDTO queryApiHistoryInfo(@PathVariable Long id) {
        return entryPointUseCase.queryApiHistoryInfo(new TraceId(id));
    }

    /**
     * 删除接口
     */
    @DeleteMapping("/entrypoint/{id}")
    public BooleanWrapper deleteEntryPoint(@PathVariable Long id) {
        entryPointUseCase.deleteEntryPoint(id);
        return BooleanWrapper.TRUE;
    }

    /**
     * 链路列表信息查询
     */
    @GetMapping("/trace")
    public List<TraceItemDTO> queryApiTraceInfoList(
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer size,
        @RequestParam(required = false) String path,
        @RequestParam(required = false) String instanceName) {
        return entryPointUseCase.queryTraceInfoList(page, size, path, instanceName);
    }

    /**
     * 链路节点列表查询
     */
    @GetMapping("/trace/node")
    public List<TraceNodeDTO> queryTraceNodeList(@RequestParam(required = false) String spanId) {
        return entryPointUseCase.queryTraceNodeList(spanId);
    }

    /**
     * 文件下载功能
     */
    @GetMapping("/entrypoint/file")
    public ResponseEntity<Resource> downloadFile(@RequestParam(required = false) String fileName) {
        byte[] fileBytes = entryPointUseCase.getDownloadFile(fileName);
        return WebUtils.getDownloadFileHttpResponse(fileBytes, fileName);
    }

}
