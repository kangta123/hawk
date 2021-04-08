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

import io.micrometer.core.instrument.util.IOUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import org.springframework.core.io.Resource;

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
    public List<UserGroupEntryPointDTO> queryApiByPath(@RequestParam(required = false) String key) {
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
    @GetMapping("/history/page")
    public TraceResponseDTO queryApiHistoryList(@RequestParam(required=false) Integer page,
            @RequestParam(required=false) Integer size,
            @RequestParam(required=false) Long entryPointId) {
        return entryPointUseCase.queryApiHistoryList(page,size,entryPointId);
    }

    /**
     * 根据历史请求id,查询单个历史请求信息
     */
    @GetMapping("/history/{id}")
    public TraceDetailDTO queryApiHistoryInfo(@PathVariable Long id) {
        return entryPointUseCase.queryApiHistoryInfo(new TraceId(id));
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
     * 文件下载功能
     */
    @GetMapping("/file")
    public ResponseEntity<Resource> downloadFile(@RequestParam(required=false) String fileName) {
        byte[] fileBytes = entryPointUseCase.getDownloadFile(fileName);
        return WebUtils.getDownloadFileHttpResponse(fileBytes,fileName);
    }
    
}
